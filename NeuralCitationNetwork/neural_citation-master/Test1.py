from __future__ import print_function
import torch
from torch import nn
import torch.nn.functional as F
from torch import Tensor

from typing import Union, List

Filters = List[int]

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

class TDNN(nn.Module):
    def __init__(self, filter_size: int, 
                       embed_size: int, 
                       num_filters: int):
        super().__init__()
        self.conv = nn.Conv2d(1, num_filters, kernel_size=(embed_size, filter_size), bias=False)
        #self.conv = nn.Conv2d(1, num_filters, kernel_size=(128, 4), bias=False)

    def forward(self, x: Tensor) -> Tensor:
        x = x.permute(0, 2, 1)        
        x = x.unsqueeze(1)
        x = F.relu(self.conv(x))
        pool_size = x.shape[-1]
        #x = F.max_pool2d(x, kernel_size=pool_size)
        x = F.max_pool2d(x, kernel_size=(1,pool_size)) #Thi add

        return x.permute(0, 2, 1, 3)

class TDNNEncoder(nn.Module):    
    def __init__(self, filters: Filters,
                       num_filters: int,
                       embed_size: int):
        super().__init__()
        self.filter_list = filters
        self.num_filters = num_filters
        self._num_filters_total = len(filters)*num_filters

        self.encoder = nn.ModuleList([TDNN(filter_size=f, embed_size = embed_size, num_filters=num_filters).to(DEVICE) 
                                        for f in self.filter_list])
        self.fc = nn.Linear(self._num_filters_total, self._num_filters_total)


    def forward(self, x: Tensor) -> Tensor:
        x = [encoder(x) for encoder in self.encoder]
        assert len(set([e.shape[0] for e in x])) == 1, "Batch sizes don't match!"

        x = torch.cat(x, dim=1).squeeze(3)
        batch_size = x.shape[0]        
        #x = x.view(batch_size, -1)
        x = x.reshape(batch_size, -1) #Thi add
        x = torch.tanh(self.fc(x))        
        return x.view(len(self.filter_list), -1, self.num_filters)

if __name__ == '__main__':
    filter_size = 4
    embed_size = 128
    num_filters = 256

    #x = torch.rand(64, 4, 128)    
    #print(x) 
    #tdnnEncoder =  TDNNEncoder(filters=[4,4,5,6,7], num_filters=num_filters, embed_size = embed_size)
    #x = tdnnEncoder(x)
    #print(x)    

from ncn.evaluation import Evaluator
from ncn.data import get_datasets
import numpy as np
from torchtext.data import BucketIterator
from ncn.core import IteratorData

#path_to_weights = "./models/NCN_9_4_4_128_filters.pt"
path_to_weights = "models/NCN_3_13_15_embed_128_hid_128_2_GRU_epoch_20_num_layers_2_split_403030.pt"

#path_to_data = "/home/jupyter/tutorials/seminar_kd/arxiv_data.csv"
path_to_data = "ncn/arxiv_data_4.csv"

data = get_datasets(path_to_data, 20000, 20000, 20000)
#NCNdata = get_bucketized_iterators("ncn/arxiv_data_4.csv",
#                                    batch_size = 64,
#                                    len_context_vocab = 20000,
#                                    len_title_vocab = 20000,
#                                    len_aut_vocab = 20000)

# create bucketted iterators for each dataset
train_iterator, valid_iterator, test_iterator = BucketIterator.splits((data.train, data.valid, data.test), 
                                                                          batch_size = 64,
                                                                          sort_within_batch = True,
                                                                          sort_key = lambda x : len(x.title_cited))
    
NCNdata = IteratorData(data.cntxt, data.ttl, data.aut, train_iterator, valid_iterator, test_iterator)    

evaluator = Evaluator([4,4,5,6,7], [1,2], 128, 128, 2, path_to_weights, data, NCNdata, evaluate=True, show_attention=False)

at_10 = evaluator.recall(10)

#round(at_10, 4)
print("NCN_3_13_15_embed_128_hid_128_2_GRU_epoch_20_num_layers_2_split_403030.pt recall(10) = ", round(at_10, 4))
print("Evaluation completed")
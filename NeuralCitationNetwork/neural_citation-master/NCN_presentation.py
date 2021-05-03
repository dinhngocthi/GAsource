import torch
from torchtext.data import BucketIterator
from ncn.evaluation import Evaluator
from ncn.data import get_datasets
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import numpy as np
#%matplotlib inline

#path_to_weights = "/home/timo/Downloads/ncn/model/NCN_9_4_4_128_filters.pt"
path_to_weights = "../models/NCN_9_4_4_128_filters.pt"
#path_to_data = "/home/timo/DataSets/KD_arxiv_CS/arxiv_data.csv"
path_to_data = "ncn/arxiv_data.csv"

def display_attention(title, attention):
    
    fig = plt.figure(figsize=(10,10))
    ax = fig.add_subplot(111)
    
    attention = attention.squeeze(1).cpu().detach().numpy()
    
    cax = ax.matshow(attention, cmap="plasma")
   
    ax.tick_params(labelsize=15)
    xlabels = ["Context TDNN 1 (f=4)", "Context TDNN 2 (f=4)", "Context TDNN 3 (f=5)", 
               "Context TDNN 3 (f=6)", "Context TDNN 3 (f=7)",
               "Citing TDNN 1 (f=1)", "Citing TDNN 2 (f=2)",
               "Cited TDNN 1 (f=1)", "Cited TDNN 2 (f=2)"
              ]
    ax.set_xticklabels([''] + xlabels, 
                       rotation=90)
    # have tokenized title here
    ax.set_yticklabels([''] + title)

    ax.xaxis.set_major_locator(ticker.MultipleLocator(1))
    ax.yaxis.set_major_locator(ticker.MultipleLocator(1))
    fig.colorbar(cax)
    plt.tight_layout()
    fig.savefig("Attention_weights.jpg", DPI=400)

    context = "Neural networks are really cool, especially if they are convolutional."
    authors = "Chuck Norris, Bruce Lee"

    tokenized = data.cntxt.tokenize(context)
    data.cntxt.numericalize([tokenized])

    examples = [example.context for example in data.train.examples[:30]]
    tensorized = data.cntxt.numericalize(data.cntxt.pad(examples))
    print(f"Batch data type: {tensorized.type()}")
    print(f"Batch shape: {tensorized.shape}")
    print(tensorized[:3, :])

    train_iterator, valid_iterator, test_iterator = BucketIterator.splits((data.train, data.valid, data.test), 
                                                                          batch_size = 32,
                                                                          sort_within_batch = True,
                                                                          sort_key = lambda x : len(x.title_cited))

    train_iterator, valid_iterator, test_iterator = BucketIterator.splits((data.train, data.valid, data.test), 
                                                                          batch_size = 32,
                                                                          sort_within_batch = True,
                                                                          sort_key = lambda x : len(x.title_cited))

    batch = next(iter(train_iterator))

    batch.title_cited.permute(1,0)[:5]

    evaluator = Evaluator([4,4,5,6,7], [1,2], 128, 128, 2,  path_to_weights, data, evaluate=False, show_attention=True)

    context = "Neural networks are really cool, especially if they are convolutional."
    authors = "Chuck Norris, Bruce Lee"
    recomms, a = evaluator.recommend(context, authors, 10)

    rec = 1
    seq = data.cntxt.tokenize(recomms[rec])
    display_attention(seq, a[1:len(seq)+1, rec, :])
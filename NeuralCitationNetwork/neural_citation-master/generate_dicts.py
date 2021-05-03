import pandas as pd
import random
import re
import spacy
import string
from typing import List, Set
from functools import partial
from spacy.tokenizer import Tokenizer
from torchtext.data import Field, BucketIterator, Dataset, TabularDataset
import matplotlib.pyplot as plt
#%matplotlib inline
import numpy as np
import nltk

import logging

logging.basicConfig(level=logging.INFO, style='$')
logger = logging.getLogger("neural_citation")
"""Base logger for the neural citation package."""

nlp = spacy.load("en_core_web_lg")
tokenizer = Tokenizer(nlp.vocab)

def get_stopwords() -> Set:
    """
    Returns spacy and nltk stopwords unified into a single set.   
    
    ## Output:  
    
    - **STOPWORDS** *(Set)*: Set containing the stopwords for preprocessing 
    """
    STOPWORDS = spacy.lang.en.stop_words.STOP_WORDS
    nltk_stopwords = set(nltk.corpus.stopwords.words('english'))
    STOPWORDS.update(nltk_stopwords)
    return STOPWORDS

def title_context_preprocessing(text: str, tokenizer: Tokenizer, identifier:str) -> List[str]:
    """
    Applies the following preprocessing steps on a string:  
 
    1. Replace digits
    2. Remove all punctuation.  
    3. Tokenize.  
    4. Remove numbers.  
    5. Lemmatize.   
    6. Remove blanks  
    7. Prune length to max length (different for contexts and titles)  
    
    ## Parameters:  
    
    - **text** *(str)*: Text input to be processed.  
    - **tokenizer** *(spacy.tokenizer.Tokenizer)*: SpaCy tokenizer object used to split the string into tokens.      
    - **identifier** *(str)*: A string determining whether a title or a context is passed as text.  

    ## Output:  
    
    - **List of strings**:  List containing the preprocessed tokens.
    """
    text = re.sub("\d*?", '', text)
    text = re.sub("[" + re.escape(string.punctuation) + "]", " ", text)
    text = [token.lemma_ for token in tokenizer(text) if not token.like_num]
    text = [token for token in text if token.strip()]

    # return the sequence up to max length or totally if shorter
    # max length depends on the type of processed text
    if identifier == "context":
        try:
            return text[:100]
        except IndexError:
            return text
    elif identifier == "title_cited":
        try:
            return text[:30]
        except IndexError:
            return text
    else:
        raise NameError("Identifier name could not be found.")


def author_preprocessing(text: str) -> List[str]:
    """
    Applies the following preprocessing steps on a string:  

    
    1. Remove all numbers.   
    2. Tokenize.  
    3. Remove blanks.  
    4. Prune length to max length. 
    
    ## Parameters:  
    
    - **text** *(str)*: Text input to be processed.  
    
    ## Output:  
    
    - **List of strings**:  List containing the preprocessed author tokens. 
    """
    text = re.sub("\d*?", '', text)
    text = text.split(',')
    text = [token.strip() for token in text if token.strip()]

    # return the sequence up to max length or totally if shorter
    try:
        return text[:5]
    except IndexError:
        return text

STOPWORDS = get_stopwords()
cntxt_tokenizer = partial(title_context_preprocessing, tokenizer=tokenizer, identifier="context")
ttl_tokenizer = partial(title_context_preprocessing, tokenizer=tokenizer, identifier="title_cited")

# instantiate fields preprocessing the relevant data
TTL = Field(tokenize=ttl_tokenizer, 
            stop_words=STOPWORDS,
            init_token = '<sos>', 
            eos_token = '<eos>',
            lower=True)

AUT = Field(tokenize=author_preprocessing, batch_first=True, lower=True)

CNTXT = Field(tokenize=cntxt_tokenizer, stop_words=STOPWORDS, lower=True, batch_first=True)

logger.info("Getting fields...")
# generate torchtext dataset from a .csv given the fields for each datatype
# has to be single dataset in order to build proper vocabularies
logger.info("Loading dataset...")
#dataset = TabularDataset("arxiv_data.csv", "CSV", 
dataset = TabularDataset("D:/Thi.DN/PhD/ThayBay/GCN/ebookML_src-master/src/neural_citation-master/ncn/arxiv_data_2.csv", "CSV", 
                   [("context", CNTXT), ("authors_citing", AUT), ("title_cited", TTL), ("authors_cited", AUT)],
                   skip_header=True)

# build field vocab before splitting data
logger.info("Building vocab...")
TTL.build_vocab(dataset, max_size=20000)
AUT.build_vocab(dataset, max_size=20000)
CNTXT.build_vocab(dataset, max_size=20000)

examples = dataset.examples
#len(examples)

import pickle

def get_aut_matchings(examples):
    mapping = {}
    for example in examples:
        key = tuple(example.title_cited)
        if key not in mapping.keys():
            mapping[key] = example.authors_cited
    
    return mapping

mapping_aut = get_aut_matchings(examples)

#with open("title_to_aut_cited.pkl", "wb") as fp:
with open("title_to_aut_cited_Thi.pkl", "wb") as fp:
    pickle.dump(mapping_aut, fp)

#dat = pd.read_csv("D:/Thi.DN/PhD/ThayBay/GCN/ebookML_src-master/src/neural_citation-master/ncn/arxiv_data.csv")
#dat["ttl_proc"] = dat["title_cited"].map(lambda x: TTL.preprocess(x)
#dat[["ttl_proc", "title_cited"]].head(10)

#def title_to_full(data):
#    mapping = {}
#   for index in data.index:
#        key = " ".join(data.iloc[index, 4])
#        if key not in mapping.keys():
#            mapping[key] = data.iloc[index, 2]
    
#    return mapping

#mapping_titles = title_to_full(dat)

#with open("title_tokenized_to_full_Thi.pkl", "wb") as fp:
#    pickle.dump(mapping_titles, fp)
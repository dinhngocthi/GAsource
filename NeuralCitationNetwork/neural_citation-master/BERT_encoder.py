import sys

import torch
from transformers import BertTokenizer, BertModel

class BERT_Text2Vec_Model(object):

    def __init__(self):
        # Load pre-trained model tokenizer (vocabulary)
        self.bertTokenizer = BertTokenizer.from_pretrained('bert-base-uncased')

        # Load pre-trained model (weights)
        self.bertModel = BertModel.from_pretrained('bert-base-uncased', output_hidden_states=True)

        # Put the model in "evaluation" mode, meaning feed-forward operation.
        self.bertModel.eval()
    '''
    def learn_word_representation(self, sentence):
        # Split the sentence into tokens
        indexed_tokens = self.bertTokenizer.encode(sentence, add_special_tokens=True, truncation=True)

        # Mark each of token as belonging to sentence "1".
        segments_ids = [1] * len(indexed_tokens)

        # Convert inputs to PyTorch tensors
        tokens_tensor = torch.tensor([indexed_tokens])
        segments_tensors = torch.tensor([segments_ids])

        with torch.no_grad():
            outputs = self.bertModel(tokens_tensor, segments_tensors)
            # `hidden_states` has shape [13 x 1 x number_token_in_sentence x 768]
            hidden_states = outputs[2]

        token_embeddings = torch.stack(hidden_states, dim=0)
        token_embeddings = torch.squeeze(token_embeddings, dim=1)
        token_sum_vecs = []
        for token in token_embeddings:
            # `token` is a [12 x 768] tensor
            # Sum the vectors from the all BERT's layers.
            token_sum_vec = torch.sum(token[-12:], dim=0)
            token_sum_vecs.append(token_sum_vec.numpy().reshape(768, 1))

        return token_sum_vecs
    '''
    def learn_BERT_sent_representation(self, sentence):
        # Split the sentence into tokens
        indexed_tokens = self.bertTokenizer.encode(sentence, add_special_tokens=False, truncation=True)

        # Mark each of token as belonging to sentence "1".
        segments_ids = [1] * len(indexed_tokens)

        # Convert inputs to PyTorch tensors
        tokens_tensor = torch.tensor([indexed_tokens])
        segments_tensors = torch.tensor([segments_ids])

        with torch.no_grad():
            outputs = self.bertModel(tokens_tensor, segments_tensors)
            # `hidden_states` has shape [13 x 1 x number_token_in_sentence x 768]
            hidden_states = outputs[2]

        # `token_vecs` is a tensor with shape [number_token_in_sentence x 768]
        token_vecs = hidden_states[-2][0]

        # Calculate the average of all token vectors.
        sentence_embedding = torch.mean(token_vecs, dim=0)

        return sentence_embedding

def main():
    sample_documents = [
        'I love deep learning',
        'Deep learning is awesome'
    ]

    model = BERT_Text2Vec_Model()
    for document in sample_documents:
        # learn the representation of document as embedding vector 1 x  768 (default of pre-trained BERT model)
        doc_emb = model.learn_BERT_sent_representation(document)
        print('{} -> {}'.format(document, doc_emb.size()))
    

if __name__ == "__main__":
    sys.exit(main())


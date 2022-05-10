from ncn.model import *
from ncn.training import *

random.seed(SEED)
torch.manual_seed(SEED)
torch.backends.cudnn.deterministic = True

if __name__ == '__main__':
    # set up training
    data = get_bucketized_iterators("ncn/arxiv_data_4.csv",
                                    batch_size = 64,
                                    len_context_vocab = 20000,
                                    len_title_vocab = 20000,
                                    len_aut_vocab = 20000)
                                    #len_context_vocab = 20,
                                    #len_title_vocab = 20,
                                    #len_aut_vocab = 20)
    PAD_IDX = data.ttl.vocab.stoi['<pad>']
    cntxt_vocab_len = len(data.cntxt.vocab)
    aut_vocab_len = len(data.aut.vocab)
    ttl_vocab_len = len(data.ttl.vocab)

    net = NeuralCitationNetwork(data,
                                context_filters=[4,4,5,6,7],
                                author_filters=[1,2],
                                context_vocab_size=cntxt_vocab_len,
                                title_vocab_size=ttl_vocab_len,
                                author_vocab_size=aut_vocab_len,
                                pad_idx=PAD_IDX,
                                #num_filters=256,
                                num_filters=128,
                                authors=True,
                                embed_size=128,
                                num_layers=2,
                                #hidden_size=256,
                                hidden_size=128,
                                dropout_p=0.2,
                                show_attention=False)
    net.to(DEVICE)

    train_losses, valid_losses = train_model(model = net, 
                                            train_iterator = data.train_iter, 
                                            valid_iterator = data.valid_iter,
                                            lr = 0.001,
                                            pad = PAD_IDX,
                                            model_name = "embed_128_hid_128_2_GRU_epoch_20_num_layers_2_split_403030")
    print("Training completed")
from ncn.model import *
from ncn.training import *

random.seed(SEED)
torch.manual_seed(SEED)
torch.backends.cudnn.deterministic = True

MODEL_NAME = "NCN_AddTitle2_embedsize=196"

# set up training
data = get_bucketized_iterators("ncn/arxiv_data_XXX.csv",
                                batch_size = 64,
                                len_context_vocab = 20000,
                                len_title_vocab = 20000,
                                len_aut_vocab = 20000)

PAD_IDX = data.ttl.vocab.stoi['<pad>']
cntxt_vocab_len = len(data.cntxt.vocab)
aut_vocab_len = len(data.aut.vocab)
ttl_vocab_len = len(data.ttl.vocab)

#Since the titles would be between context and authors in length, 
# I would use maybe something like [2, 2, 3] or [2, 3, 4]. 
# The length of the list [x, y, z] is the number of TDNN encoders, 
# where the numbers are the filter size of those TDNNs. 
# Since the titles are longer than authors but shorter than the contest, 
# having the length of the list be 3 and using filter sizes between those of authors and context makes sense to me.
net = NeuralCitationNetwork(context_filters=[4,4,5,6,7],
                            title_filters=[2,2,3],  #Thi added
                            author_filters=[1,2],
                            context_vocab_size=cntxt_vocab_len,
                            title_vocab_size=ttl_vocab_len,
                            author_vocab_size=aut_vocab_len,
                            pad_idx=PAD_IDX,
                            #num_filters=256,
                            num_filters=128,
                            authors=True,
                            #embed_size=128,
                            embed_size=196,  # Thi added
                            num_layers=1,
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
                                         #model_name = "NCN_AddTitle2_embedsize=164")
                                         model_name = MODEL_NAME)
         
print("Training completed")
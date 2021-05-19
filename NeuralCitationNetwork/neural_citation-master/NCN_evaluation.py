from ncn.evaluation import Evaluator
from ncn.data import get_datasets
import numpy as np

#path_to_weights = "../models/NCN_9_4_4_128_filters_org.pt"
path_to_weights = "models/NCN_5_18_13_embed_128_hid_128_1_GRU.pt"

#path_to_data = "/home/jupyter/tutorials/seminar_kd/arxiv_data.csv"
path_to_data = "ncn/arxiv_data.csv"

data = get_datasets(path_to_data, 20000, 20000, 20000)

#evaluator = Evaluator([4,4,5,6,7], [1,2], 128, 128, 2, path_to_weights, data, evaluate=True, show_attention=False)
evaluator = Evaluator([4,4,5,6,7], [2,2,3], [1,2], 128, 128, 2, path_to_weights, data, evaluate=True, show_attention=False) #Thi added

at_10 = evaluator.recall(10)

round(at_10, 4)

print("Evaluation completed")
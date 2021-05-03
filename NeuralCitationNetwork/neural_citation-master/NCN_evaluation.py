from ncn.evaluation import Evaluator
from ncn.data import get_datasets
import numpy as np

#path_to_weights = "./models/NCN_9_4_4_128_filters.pt"
path_to_weights = "../models/NCN_9_4_4_128_filters_org.pt"
#path_to_weights = "../models/NCN_8_29_6_Ebesu_Fang.pt"

#path_to_data = "/home/jupyter/tutorials/seminar_kd/arxiv_data.csv"
path_to_data = "ncn/arxiv_data_4.csv"

data = get_datasets(path_to_data, 20000, 20000, 20000)

evaluator = Evaluator([4,4,5,6,7], [1,2], 128, 128, 2, path_to_weights, data, evaluate=True, show_attention=False)

at_10 = evaluator.recall(10)

round(at_10, 4)
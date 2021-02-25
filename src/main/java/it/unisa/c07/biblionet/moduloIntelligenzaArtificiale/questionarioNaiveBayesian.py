import pandas as pd
from pgmpy.models import BayesianModel
from pgmpy.estimators import MaximumLikelihoodEstimator
from random import randint

questions_list = []
while len(questions_list) < 4:
    value = randint(0, 7)
    if not questions_list.__contains__(value):
        questions_list.append(value)
questions_list.append(8)
questions_list.append(9)
questions_list.append(10)
questions_list.append(11)
print(questions_list)

excel = pd.read_excel(r'dataset/prova.xlsx', usecols=questions_list)
dataset = pd.DataFrame(excel)
dataset = dataset.dropna()


dataset.columns.values[0] = "user_answer"
dataset.columns.values[1] = "user_answer1"
dataset.columns.values[2] = "user_answer2"
dataset.columns.values[3] = "user_answer3"
dataset.columns.values[4] = "car_genre"
dataset.columns.values[5] = "car_genre1"
dataset.columns.values[6] = "car_genre2"
dataset.columns.values[7] = "genre"

print(dataset)

model = BayesianModel([('user_answer', 'car_genre'), ('user_answer', 'car_genre1'), ('user_answer', 'car_genre2'),
                       ('user_answer1', 'car_genre'), ('user_answer1', 'car_genre1'), ('user_answer1', 'car_genre2'),
                       ('user_answer2', 'car_genre'), ('user_answer2', 'car_genre1'), ('user_answer2', 'car_genre2'),
                       ('user_answer3', 'car_genre'), ('user_answer3', 'car_genre1'), ('user_answer3', 'car_genre2'),
                       ('car_genre', 'genre'), ('car_genre1', 'genre'), ('car_genre2', 'genre')])

model.fit(dataset, MaximumLikelihoodEstimator)
print("Proprietà Rete Baeysiana rispettate:", model.check_model())
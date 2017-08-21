#######################################################3
library("nnet")

dataset <- read.csv("sample_net_2.csv")
trainset <- dataset[sample(1:50, 17),]

n = 52
creditnet <- nnet(class ~ social_media_score + loan_history_score + balance_score + income_score + repay_score, dataset, size=4, linout=TRUE, skip=TRUE 
)

predict_result <- predict(creditnet, newdata= dataset[,1:5])
p_result <- predict_result[,1]
print(p_result)

result <- 0

for (i in 1:n) { 
  print(p_result[i])
  
  if (p_result[i] > 1500) {
    result[i] = '2000'
    
  }
  
  else if ((p_result[i] > 575) && (p_result[i] < 1500)) {
    result[i] = '1000'
    
  }
  
  else
    result[i] = '500'
  
}


print(result)


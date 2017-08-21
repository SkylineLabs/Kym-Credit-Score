library(mongolite)
library(twitteR)
library(stringr)
library(tm)
require('sentR')
library(plyr)

users <- mongo(collection = "users", db = "r_db", url = "mongodb://localhost", verbose = TRUE)


update_finance_data <- function() {
  users_info <- users$find()
  
  for(name in users_info[[1]]) {
    #users$update( query = paste0('{"name":"', name,'"}'), update = paste0('{"$set":{"tweets_sentiment": ', get_tweets_sentiment(twitter_handle), '}}'))
    account <- mongo(collection = name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
    
    transactions_info <- account$aggregate(
      '
      [ { "$group": { "_id": null, "total": { "$sum": "$amount" } } } ]
      '
    )
    print(transactions_info)
  }
  
  #transcation_type <- transactions_info[1]
  #transcation_amount <- transactions_info[2]
  #transcation_bank_name <- transactions_info[4]
  
  
}


update_finance_data()

#users_info <- users$find()
#print(users_info)

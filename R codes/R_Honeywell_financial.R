library(mongolite)
library(twitteR)
library(stringr)
library(tm)
library(plyr)
library(rjson)
library(httr)
library(RJSONIO) 


users <- mongo(collection = "users", db = "r_db", url = "mongodb://localhost", verbose = TRUE)
user_names <- mongo(collection = "user_info", db = "r_db", url = "mongodb://localhost", verbose = TRUE)
currentDate<-Sys.Date()
month<-format(currentDate,"%m")
year<-format(currentDate,"%Y")

###########################################Financial data###############################################

update_bank_summary <- function(name) {
  account <- mongo(collection = name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  
  each_bank_balance <- account$aggregate( 
    
    '[{"$match":{"account_number": { "$exists": true}}},{"$group":{"_id":"$bank_name", "balance": {"$last":"$balance"}}}]' 
    
  )
  
  print(each_bank_balance)
  
  if(dim(each_bank_balance)[1] > 0 && dim(each_bank_balance)[2] > 0)
  {
    sum = 0
    for(balance in each_bank_balance[,2]) {
      sum <- sum + balance
    }
    no_of_accounts <- dim(each_bank_balance)[1]
    print(no_of_accounts)
    users$update(query = paste0('{"name":"', name,'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"no_of_accounts": ', no_of_accounts, '}}'))
    
  }
  
  each_accounts_balance <- account$aggregate( 
    
    '[{"$match":{"account_number": { "$exists": true}}},{"$group":{"_id":"$account_number", "balance": {"$last":"$balance"}}}]' 
    
  )
  if(dim(each_accounts_balance)[1] > 0 && dim(each_accounts_balance)[2] > 0)
  {
    sum = 0
    for(balance in each_accounts_balance[,2]) {
      sum <- sum + balance
    }
    users$update(query = paste0('{"name":"', name, '","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"total_bank_balance": ', sum, '}}'))
    no_of_accounts <- dim(each_accounts_balance)[1]
    print(no_of_accounts)
    users$update(query = paste0('{"name":"', name, '","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"no_of_accounts": ', no_of_accounts, '}}'))
  }
}



##################################3


update_banks <- function(name) {
  account <- mongo(collection = name , db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  each_account_credit_summary <- account$aggregate( 
    
    paste0('[ {"$match": {"account_number": { "$exists": true}, "type":"credit", "month" : ', as.integer(month) , ',"year" :', year, '}},
           {"$group":{"_id":{"account_number":"$account_number"}, "amount": {"$sum":"$amount"}, "number_of_transcations": {"$sum":1} }}]')
    )
  each_account_debit_summary <- account$aggregate( 
    
    paste0('[ {"$match": {"account_number": { "$exists": true}, "type":"debit", "month" : ', as.integer(month) , ',"year" :', year, '}},
           {"$group":{"_id":{"account_number":"$account_number"}, "amount": {"$sum":"$amount"}, "number_of_transcations": {"$sum":1} }}]')
    )
  each_account_balance_bankname <- account$aggregate(
    '
    [ {"$match": {"account_number": { "$exists": true}}},
    {"$group":{"_id":{"account_number":"$account_number"}, "balance": {"$last":"$balance"},"bank_name": {"$last":"$bank_name"} }}]
    '
  )
  
  bank_collection_name <- paste(name,"_summary",sep="")
  summary_col <- mongo(collection = bank_collection_name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  
  #print(each_account_balance_bankname)
  #print(each_account_credit_summary)
  #print(each_account_debit_summary)
  nrows <- nrow(each_account_balance_bankname)
  for(i in 1:nrows){
    
    account_number <- each_account_balance_bankname[i,1]
    balance <- each_account_balance_bankname[i,2]
    bank_name <-  each_account_balance_bankname[i,3]
    
    summary_col$update(query = paste0('{"account_number":"', account_number[[1]],'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"type" : "account", "balance": ', balance , ',"bank_name":"', bank_name,'"}}'), upsert = TRUE)
    
  }
  
  nrows <- nrow(each_account_credit_summary)
  if(nrows > 0) {
    for(i in 1:nrows){
      
      account_number <- each_account_credit_summary[i, 1]
      credit_amount <- each_account_credit_summary[i,2]
      credit_no_of_transactions <- each_account_credit_summary[i,3]
      #print(account_number)
      #print(credit_no_of_transactions)
      
      summary_col$update(query = paste0('{"account_number":"', account_number[[1]],'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"total_credit_amount": ', credit_amount , ',"total_credit_transactions":', credit_no_of_transactions,'}}'), upsert = TRUE)
      
    }
  }
  nrows <- nrow(each_account_debit_summary)
  if(nrows > 0) {
    for(i in 1:nrows){
      
      account_number <- each_account_debit_summary[i, 1]
      debit_amount <- each_account_debit_summary[i,2]
      debit_no_of_transactions <- each_account_debit_summary[i,3]
      #print(account_number)
      #print(credit_no_of_transactions)
      
      summary_col$update(query = paste0('{"account_number":"', account_number[[1]],'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"total_debit_amount": ', debit_amount , ',"total_debit_transactions":', debit_no_of_transactions,'}}'), upsert = TRUE)
      
    }
  }
  
}




update_finance_credit_debit <- function(name, twitter_handle) {
  #users$update( query = paste0('{"name":"', name,'"}'), update = paste0('{"$set":{"tweets_sentiment": ', get_tweets_sentiment(twitter_handle), '}}'))
  account <- mongo(collection = name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  
  transactions_info_credit <- account$aggregate(
    
    paste0( '[{"$match":{"type":"credit", "month" : ', as.integer(month) , ',"year" :', year, '}},{"$group":{"_id":"$type", "count": {"$sum":1}, "amount":{"$sum":"$amount"},"average":{"$avg":"$amount"}}}]' )
    
  )
  #, "month" : "10", "year" : "2016"
  transactions_info_debit <- account$aggregate(
    paste0( '[{"$match":{"type":"debit", "month" : ', as.integer(month) , ',"year" :', year, '}},{"$group":{"_id":"$type", "count": {"$sum":1}, "amount":{"$sum":"$amount"},"average":{"$avg":"$amount"}}}]' )
  )
  users$update(query = paste0('{"name":"', name, '","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"twitter_handle":"', twitter_handle, '"}}'), upsert = TRUE)
  
  if(dim(transactions_info_credit)[1] > 0 && dim(transactions_info_credit)[2] > 0)
  {
    users$update(query = paste0('{"name":"', name, '","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"number_transactions_credit": ', transactions_info_credit[2], '}}'), upsert = TRUE)
    users$update(query = paste0('{"name":"', name, '","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"totalamount_transactions_credit": ', transactions_info_credit[3], '}}'), upsert = TRUE)
    users$update(query = paste0('{"name":"', name, '","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"averageamount_transactions_credit": ', as.integer(transactions_info_credit[4]), '}}'), upsert = TRUE)
    
  }
  if(dim(transactions_info_debit)[1] > 0 && dim(transactions_info_debit)[2] > 0)
  {
    users$update(query = paste0('{"name":"', name,'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"averageamount_transactions_debit": ', as.integer(transactions_info_debit[4]), '}}'), upsert = TRUE)
    users$update(query = paste0('{"name":"', name,'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"number_transactions_debit": ', transactions_info_debit[2], '}}'), upsert = TRUE)
    users$update(query = paste0('{"name":"', name,'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"totalamount_transactions_debit": ', transactions_info_debit[3], '}}'), upsert = TRUE)
  }
}





update_location_data <- function(name) {
  account <- mongo(collection = name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  city_not_fetched <- account$aggregate(
    '
    [{"$match":{"type":"location", "country": {"$exists": false}}}]
    '
  )
  
  if(dim(city_not_fetched)[1] > 0 && dim(city_not_fetched)[2] > 0)
  {
    nrows <- nrow(city_not_fetched)
    for(i in 1:nrows){
      
      print(city_not_fetched)
      
      latitude <- city_not_fetched[i,3]
      longitude <- city_not_fetched[i,4]
      
      latlon <- paste(latitude,',',longitude,sep="")
      print(latlon)
      
      API_Key <- "AIzaSyAZI9adLQIuSi2-YESdSwlqJj087KH3DrU"
      #Google Maps API key skylinelabs account  AIzaSyAZI9adLQIuSi2-YESdSwlqJj087KH3DrU
      
      
      connectStr <- paste("https://maps.google.com/maps/api/geocode/json?key=",API_Key,"&latlng=",latlon, sep="")
      con <- url(connectStr)
      data.json <- fromJSON(paste(readLines(con), collapse=""))
      close(con)
      
      #print(data.json)
      
      country <- ""
      city <- ""
      i <- 0
      
      nrows <- lengths(data.json)
      for(i in 1:nrows){
        type <- data.json$results[[i]]$types[1]
        if(identical(type,"country")){
          country <- data.json$results[[i]]$address_components[[1]]$long_name[1]
          print(country)
        }
        
        if(identical(type,"locality")){
          city <- data.json$results[[i]]$address_components[[2]]$long_name[1]
          print(city)
        }
        
        if(identical(city,"")){
          city <- data.json$results[[i]]$address_components[[3]]$long_name[1]
          print(city)
        }
      }
      
      
      
      query <- account$update(query = paste0('{"latitude":', latitude,',"longitude":',longitude,'}'), update = paste0('{"$set":{"city":"', city, '","country":"',country,'"}}'), upsert=TRUE,  multiple=TRUE )
      #m <- mongo(collection = bank_collection_name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
      #m$update(query = paste0('{"account_number":"', account_number,'","type":"',transaction_type,'"}'), update = paste0('{"$set":{"amount": ', amount, ',"transactions_number":', numer_of_transactions, ',"balance":', balance, ',"bank_name":"', bank,'"}}'),upsert = TRUE)
    }
  }
}

update_summary_location_data <- function (name) {
  collection_name <- paste(name,"_summary",sep="")
  summary_collection <- mongo(collection = collection_name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  
  name_collection <- mongo(collection = name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  city_wise_location <- name_collection$aggregate( 
    
    paste0('[ {"$match": {"type":"location", "month" : ', as.integer(month) , ',"year" :', year, '}},
           {"$group":{"_id":{"city":"$city", "country":"$country"}, "number": {"$sum":1} }}]')
    )
  print(city_wise_location)
  nrows <- nrow(city_wise_location)
  if(nrows > 0) {
    for(i in 1:nrows){
      
      city <- city_wise_location[i,1][1]
      country <- city_wise_location[i,1][2]
      number <-  city_wise_location[i,2]
      print(city[[1]])
      print(country[[1]])
      print(number)
      
      summary_collection$update(query = paste0('{"city":"', city[[1]],'","country":"',country[[1]],'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"type" : "location", "number": ', number ,'}}'), upsert = TRUE)
      
      
    }
  }
}

update_loan_payment_data <- function(name) {
  
  usercol <- mongo(collection = "users", db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  payment_collection_name <- paste(name,"_payments",sep="")
  pay_col <- mongo(collection = payment_collection_name, db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  payments_info <- pay_col$aggregate(
    '[{"$match":{"type":"EMI" }},{"$group":{"_id":"$type","sum_default": {"$sum": "$default_payment"}, "sum_timely":{"$sum":"$timely_payment"}}}]'
  )
  print(payments_info)
  nrows <- nrow(payments_info)
  if(nrows == 1) {
    ndefaults <- payments_info[1,2]
    ntimely <- payments_info[1,3]
    loan_hist_score <- as.integer(ntimely) / (as.integer(ntimely) + as.integer(ndefaults)) * 100
    print(loan_hist_score)
    usercol$update(query = paste0('{"name":"', name,'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"loan_history_score": ', loan_hist_score, '}}'), upsert = TRUE)
    
    
  }
  
  
}

print_userdb <- function(){
  users_info <- users$find()
  print(users_info)
}

update_wallet_repay_score <- function(name) {
  usercol <- mongo(collection = "users", db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  userinfo <- mongo(collection = "user_info", db = "r_db", url = "mongodb://localhost", verbose = TRUE)
  
  res <- usercol$aggregate(paste0( '[{"$match":{"name":"', name, '" }},
           {"$group":{"_id":{"name":"$name"}, "number": {"$sum":1} }}]' ))
  current_no_of_months <- res[1, "number"]

  uinf <- userinfo$find(paste0('{"name":"', name, '"}'))
  wallet_balance <- uinf[1, "wallet_balance"]
  
  print(current_no_of_months)
  print(wallet_balance)
  
  #current_no_of_months = 2

  if(current_no_of_months == 1) {
    if(wallet_balance < 0) {
      repay_score = 0
    }
    else {
      repay_score = 100
      
    }
  }
  
  else {
    previous_no_of_months = current_no_of_months - 1
    print(previous_no_of_months)
    #prev_repay <- usercol$find(paste0('{"no_of_months" :', previous_no_of_months ,', "name" :"', name, '"}'))
    query <- paste0('{"name":"',name,'"}')
    prev_repay <- usercol$find(query, sort= '{"no_of_months":-1}')
    prev_repay_score <- prev_repay[2, "repay_score"]
    print(prev_repay_score)

    if(wallet_balance < 0) {
      paid = 0
    }
    else {
      paid = 1
    }
    
    repay_score = (((prev_repay_score * (current_no_of_months - 1) / 100 ) + paid ) / current_no_of_months ) * 100
    print(repay_score)
  }
  usercol$update(query = paste0('{"name":"', name,'","month" : ', as.integer(month) , ',"year" :', year, '}'), update = paste0('{"$set":{"repay_score": ', repay_score, ', "no_of_months":', current_no_of_months, '}}'), upsert = TRUE)
  
}




update_all_data <- function() {
  users_info <- user_names$find()
  
  for (i in 1:nrow(users_info)) {
    name <- users_info[i, "name"]
    twitter_handle <- users_info[i, "twitter_handle"]
    
    update_finance_credit_debit(name, twitter_handle)
    update_bank_summary(name)
    update_banks(name)
    update_location_data(name)
    update_summary_location_data(name)
    update_loan_payment_data(name)
    update_wallet_repay_score (name)
  }
}


update_all_data()



###########################################Financial data###############################################



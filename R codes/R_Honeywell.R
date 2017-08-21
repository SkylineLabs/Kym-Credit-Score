library(mongolite)
library(twitteR)
library(stringr)
library(tm)
require('sentR')
library(plyr)
library(rjson)


#################################################################################################
####################################Twitter Auth#################################################
api_key = 'RUmPyAIYfK9eMzIavtopngC21'
api_secret = 'pHIFNMYpzqzIgxnHAXdrXAU75fCg1foTruHCV3lkM8BGwTyBBM'
access_token = '2405586890-T11ug4N2p1qBFNBuNHLnc63zEsOw5vQUmqtcFAP'
access_token_secret = '59Qd4Ew50ssNeYcNfrT6HxvXUQ3ATI4A56PGLFpeMTFkz'
setup_twitter_oauth(api_key,api_secret,access_token,access_token_secret)
####################################Twitter Auth#################################################


####################################Load sentiments word file#################################################
projectDir = getwd()
dataDir = file.path(projectDir, 'data')
hu.liu.pos = scan(file.path(dataDir, 'opinion-lexicon-English', 'positive-words.txt'), what='character', comment.char=';')
hu.liu.neg = scan(file.path(dataDir, 'opinion-lexicon-English', 'negative-words.txt'), what='character', comment.char=';')
pos.words = c(hu.liu.pos, 'upgrade')
neg.words = c(hu.liu.neg, 'wtf', 'wait', 'waiting', 'epicfail', 'mechanical')
####################################Load sentiments word file#################################################
currentDate<-Sys.Date()
month<-format(currentDate,"%m")
year<-format(currentDate,"%Y")


users_summary <- mongo(collection = "user_info", db = "r_db", url = "mongodb://localhost", verbose = TRUE)
users<- mongo(collection = "users", db = "r_db", url = "mongodb://localhost", verbose = TRUE)


########################################Twitter Update##################################################
update_tweets_sentiment <- function() {
  users_info <- users_summary$find()
  for(twitter_handle in users_info[[2]]) {
    print(twitter_handle)
    users$update( query = paste0('{"twitter_handle":"', twitter_handle,'","month" : ', as.integer(month) , ',"year" :', year,'}'), update = paste0('{"$set":{"tweets_sentiment": ', get_tweets_sentiment(twitter_handle), '}}'), upsert = TRUE)
  }
}

get_tweets_sentiment <- function(twitter_handle) {
  name_mentioned <- searchTwitter(paste0("from:", twitter_handle))
  print(name_mentioned)
  tweets.text <- sapply(name_mentioned, function(x) x$getText())
  tweets.text <- gsub("rt", "", tweets.text)
  tweets.text <- gsub("[[:punct:]]", "", tweets.text)
  tweets.text <- gsub("[ |\t]{2,}", "", tweets.text)
  tweets.text <- gsub("^ ", "", tweets.text)
  tweets.text <- gsub(" $", "", tweets.text)
  tweets.text <- tolower(tweets.text)
  tweets.text.corpus <- Corpus(VectorSource(tweets.text))
  tweets.text.corpus <- tm_map(tweets.text.corpus, function(x)removeWords(x,stopwords()))
  length <- length (tweets.text)
  sentiment_score <-0
  
  for (i in 1:length){
    all_tweets_string <- tweets.text[[i]]
    word.list = str_split(all_tweets_string, '\\s+')
    words = unlist(word.list)
    pos.matches = match(words, pos.words)
    neg.matches = match(words, neg.words)
    pos.matches = !is.na(pos.matches)
    neg.matches = !is.na(neg.matches)
    score = sum(pos.matches) - sum(neg.matches)
    sentiment_score <- sentiment_score + score
    print(score)
  }
  final <- sentiment_score/length
}

update_friends_followers_count <- function() {
  users_info <- users$find()
  for(twitter_handle in users_info[[2]]) {
    users$update(query = paste0('{"twitter_handle":"', twitter_handle,'","month" : ', as.integer(month) , ',"year" :', year,'}'), update = paste0('{"$set":{"twitter_followers_count": ', get_followers_count(twitter_handle), '}}'))
    users$update(query = paste0('{"twitter_handle":"', twitter_handle,'","month" : ', as.integer(month) , ',"year" :', year,'}'), update = paste0('{"$set":{"twitter_friends_count": ', get_friends_count(twitter_handle), '}}'))
  }
}

get_followers_count <- function(twitter_handle) {
  tweet <- getUser(twitter_handle)
  twitter_number_of_followers <-tweet$getFollowersCount()
}

get_friends_count <- function(twitter_handle) {
  tweet <- getUser(twitter_handle)
  twitter_number_of_friends<-tweet$getFriendsCount()
}

update_tweets_sentiment()
update_friends_followers_count()

########################################Twitter Update##################################################




users_info <- users$find()
print(users_info)

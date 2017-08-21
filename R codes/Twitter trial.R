library(twitteR)
library(stringr)
library(tm)
require('sentR')
library(plyr)
library(qdap)

projectDir = getwd()
dataDir = file.path(projectDir, 'data')
hu.liu.pos = scan(file.path(dataDir, 'opinion-lexicon-English', 'positive-words.txt'), what='character', comment.char=';')
hu.liu.neg = scan(file.path(dataDir, 'opinion-lexicon-English', 'negative-words.txt'), what='character', comment.char=';')

pos.words = c(hu.liu.pos, 'upgrade')
neg.words = c(hu.liu.neg, 'wtf', 'wait', 'waiting', 'epicfail', 'mechanical')



api_key = 'RUmPyAIYfK9eMzIavtopngC21'
api_secret = 'pHIFNMYpzqzIgxnHAXdrXAU75fCg1foTruHCV3lkM8BGwTyBBM'
access_token = '2405586890-T11ug4N2p1qBFNBuNHLnc63zEsOw5vQUmqtcFAP'
access_token_secret = '59Qd4Ew50ssNeYcNfrT6HxvXUQ3ATI4A56PGLFpeMTFkz'

setup_twitter_oauth(api_key,api_secret,access_token,access_token_secret)

#tweet <- getUser("JayLohokare")

#twitter_number_of_followers <-tweet$getFollowersCount()
#print(twitter_number_of_followers)

#twitter_number_of_friends<-tweet$getFriendsCount()
#print(twitter_number_of_friends)

#twitter_location<-tweet$getLocation()
#print(twitter_location)

#twitter_verified<-tweet$getVerified()
#print(twitter_verified)

#twitter_description<-tweet$getDescription()
#print(twitter_description)

#twitter_lang<-tweet$getLang()
#print(twitter_lang)

#hashTag_search <- searchTwitter("#India", n=10, lang="en")
#print(hashTag_search)

name_mentioned <- searchTwitter("from:JayLohokare", n = 10)
#print(name_mentioned)

#twitter_tweets_timeline<-userTimeline("JayLohokare")
#print(twitter_tweets_timeline)




############################Start of Sentiment calculation###############################

#https://www.credera.com/blog/business-intelligence/twitter-analytics-using-r-part-2-create-word-cloud/
#https://github.com/jeffreybreen/twitter-sentiment-analysis-tutorial-201107

tweets.text <- sapply(name_mentioned, function(x) x$getText())
# Replace blank space (“rt”)
tweets.text <- gsub("rt", "", tweets.text)
# Replace @UserName
#tweets.text <- gsub("@\\w+", "", tweets.text)
# Remove punctuation
tweets.text <- gsub("[[:punct:]]", "", tweets.text)
# Remove links
#tweets.text <- gsub("http\\w+", "", tweets.text)
# Remove tabs
tweets.text <- gsub("[ |\t]{2,}", "", tweets.text)
# Remove blank spaces at the beginning
tweets.text <- gsub("^ ", "", tweets.text)
# Remove blank spaces at the end
tweets.text <- gsub(" $", "", tweets.text)
#convert all text to lower case
tweets.text <- tolower(tweets.text)
#create corpus
tweets.text.corpus <- Corpus(VectorSource(tweets.text))
#clean up by removing stop words
tweets.text.corpus <- tm_map(tweets.text.corpus, function(x)removeWords(x,stopwords()))

#positive <- c('happy', 'well-off', 'yes','appreciate', 'fortunate',  'success', 'successful', 'good', 'happiness', 'win', 'enjoy')
#negative <- c('sad', 'killed','against', 'afraid', 'sorry','no', 'not', 'depressed', 'loss', 'lost', 'bad', 'miserable', 'terrible', 'down', 'why')

length <- length (tweets.text)
sentiment_score <-0

for (i in 1:length){
  all_tweets_string <- tweets.text[[i]]

  # split into words. str_split is in the stringr package
  word.list = str_split(all_tweets_string, '\\s+')
  # sometimes a list() is one level of hierarchy too much
  words = unlist(word.list)
  
  # compare our words to the dictionaries of positive & negative terms
  pos.matches = match(words, pos.words)
  neg.matches = match(words, neg.words)
  
  # match() returns the position of the matched term or NA
  # we just want a TRUE/FALSE:
  pos.matches = !is.na(pos.matches)
  neg.matches = !is.na(neg.matches)
  
  # and conveniently enough, TRUE/FALSE will be treated as 1/0 by sum():
  score = sum(pos.matches) - sum(neg.matches)
  sentiment_score <- sentiment_score + score
  
  print(score)
  
  #out <- classify.aggregate(all_tweets_string, positive, negative)
  #print(out$score)
  
}
print(paste0("Final score is: ", sentiment_score/length))

##############################End of Sentiment calculation#############################




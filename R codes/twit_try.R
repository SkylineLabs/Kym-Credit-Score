library(twitteR)
library(stringr)
library(tm)
require('sentR')
library(plyr)

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

tweet <- getUser("JayLohokare")
ns <-updateStatus("Status through R trial")
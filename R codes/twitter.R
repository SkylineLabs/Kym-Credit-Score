api_key = 'RUmPyAIYfK9eMzIavtopngC21'
api_secret = 'pHIFNMYpzqzIgxnHAXdrXAU75fCg1foTruHCV3lkM8BGwTyBBM'
access_token = '2405586890-T11ug4N2p1qBFNBuNHLnc63zEsOw5vQUmqtcFAP'
access_token_secret = '59Qd4Ew50ssNeYcNfrT6HxvXUQ3ATI4A56PGLFpeMTFkz'

setup_twitter_oauth(api_key,api_secret,access_token,access_token_secret)

tweet <- getUser("JayLohokare")
data <-tweet$getDescription()
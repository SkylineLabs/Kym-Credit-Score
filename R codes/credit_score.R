input <- read.csv( file = "sample_net_2.csv", head = TRUE, sep = ",")

# Create the relationship model.
model <- lm(class ~ social_media_score + loan_history_score + balance_score + income_score + repay_score, data = input)

# Show the model.
print(model)

# Get the Intercept and coefficients as vector elements.
c <- coef(model)[1]
social_media_score <- coef(model)[2]
loan_history_score <- coef(model)[3]
balance_score <- coef(model)[4]
income_score <- coef(model)[5]
repay_score <- coef(model) [6]



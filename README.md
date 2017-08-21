Decentralized credit score calculation system POC. The system mines data from tweeter, SMS to calculate credit score using Machine learning.







Personalized Credit Profile for micro-loans


Introduction to solution 
The current banking system involves loans that are granted using the CIBIL scores. However, not all individuals in India have bank related data for CIBIL to be able to create a good credit profile of that individual. The proposed solution is a system which can provide micro-loans based on credit profile calculated without the CIBIL score, using data captured through smartphones.
The system is built over an e-wallet like PayTM (A dummy e-wallet will be built for the hackathon)
The data required for credit score calculation is captured through smartphone application 
All the banks, insurance providers, etc keep sending update messages (SMS) to the users from time to time. Using simple key word mining, this data can be captured and used to create a credit profile using machine learning.
To form a personalized credit score system, we need to access and assess the data and trends that are related to lifestyle and expenses of the concerned individual. Accessing this data will help us to predict the purchasing power, credibility, ability to repay loans, income to expense ratio, etc. Thus, our solution is mainly based on collecting and then analyzing data related to a person. We are primarily using Natural language processing and sentiment-trend analysis as the backbone of our idea. We plan to collect the data through the app and web-crawlers and aggregate the analysis in android application and web portal. 

Approach to constructing the product
The solution has two aspects – The 1st part captures the data and 2nd part presents the analytics and graphs. 
There are 3 technical components of the system. 
1.	Smart phone application
2.	Server for data capturing, storage and analysis
3.	Web portal for monitoring the data trends and analytics results.

Collection of data
•	SMS & Email – Android app will have NLP algorithms to collect data from SMS. This data will include details of bank transactions like adding or removing money from accounts, purchases at various ecommerce stores, insurance policies, rentals, etc. This will result to capture of all financial transactions and thus we can establish a generic idea about the expenses the person is capable of, his bank financial status, his habits, etc. 
•	Browser history and cookies – The data collected from browser history like the products searched, websites visited will help us analyze the trends in expenses. For example, a person will search for iPhones on ecommerce websites only if he is capable of purchasing it.
•	Contextual data – Data collected from smartphone app like geolocation will help us know better about the person. Suppose the person is frequently traveling abroad, he surely will have higher credit score.
•	Social media – We will crawl through the social media accounts of the person using this service. For example, we will get data from Twitter handle of the individual and then link the content in the tweets shared by the person with his financial profile. Sentiment analysis will help us understand more about the person. A negative sentiment usually will stand for a relatively lower credit score. Similarly, richness of language used in the posts indicates higher education and thus a higher probability of good financial status. Mention of costly products, places visited can give a fair idea of financial wellbeing.
 
Source of Data


Analysis of data
•	Having the data at one place is the first step to compute the credit score. We will use this data to find trends, extremities and patterns to calculate this score.
•	At very basic level, SMS and Emails will give direct details of available account balance. It will tell us about the number of accounts, average balance, etc. WE can find out many important trends using this data. For example, if the data shows that the person’s expenditure has reduced on e-commerce platforms, it suggests deteriorated financial status.
•	A person’s wish list which we get from browser history and cookies will tell us about products the person is likely to purchase in future, and thus giving us idea about his ability to purchase.
•	Sentiment analysis of social media can shed light on many aspects. A person having a positive or happy sentiment is usually financially stable and vice-versa.
•	Using the data obtained together can help us create in depth model for a person’s financial status. For example, if a person is sad or negative on social media, and account details show that the expenses are going down, against the prior trend then it might be a case that he has now entered a state of financial depression.
•	Geolocation data can tell us about the neighborhood in which the person stays. A rich area indicates higher financial status.
•	Social networks can directly indicate the job, education of the person, career trends and thus we can establish an idea about his credit score.
•	Even minute details like device which the app is installed on can be a big indicator for the financial capabilities.
•	Every data parameter captured will be normalized to a scale of 0 to 100. Machine learning will then calculate the final credit score.





Technical details for the solution:
1.	Smartphone application :
The application will keep scanning the emails, SMS real time coming to the phones. The scanning will be based on key word search, to eliminate handling data that’s redundant. If the message/email contains related data, the application will send it to the servers. There will be an offline database maintained in-case there is no internet connection. 
The NLP algorithms which will be the key elements in information retrieval, will be custom made and will work offline. We will use high encryption standards to ensure that the confidential bank data remains secure.
We will use light weight protocols like MQTT/XMPP for sending the data to servers. 
The data will be sent to servers with a fixed frequency to avoid high network and battery consumption
2.	The application will send JSONs to servers which will store the data in NoSQL databases. To ensure scalability, we will use distributed architecture on the cloud. The MQTT brokers will be highly scalable based on Erlang or Scala. We can implement a block-chain system for backend. 
3.	To implement highly scalable solution, we can involve components like Apache Kafka with load balancing proxy servers like HAProxy.
4.	The analytics will be driven by R language. R will input data from the NoSQL databases and write back the analytics to better structured database. The data will then be represented in application and web portal in from of graphs and summaries. R is the best choice due to the numerous number of libraries available in R.
5.	We will crawl social media using the open APIs (Like Facebook and Twitter API)
6.	We will use custom libraries for Sentiment analysis, NLP or any language extraction processes.
7.	Credit score can be calculated using machine learning after giving appropriate weightage to data parameters captured. (For simplicity of POC, we plan to build multiple linear regressions)

Privacy issues:
The system collects private data from SMS. This might not be acceptable to users. To avoid such issues, the proposed system involves block-chain like backend so as to anonymize the data sent to the distributed servers.

What to do with the data and analytics reports:
1.	Represent the trends and patterns graphically.
2.	Highlight anomalies in the data.
3.	Predict future trends and give tips to avoid or improve situations.
4.	Give to the users ways by which the credit score could increase.
5.	Give offers and rewards to the users based on the data collected.
6.	Feed the data back to users so that they can monitor, analyze and improve their finances, social presence and thus the credit score.

Outcomes:
1.	The users will get micro-loans without need to pay for CIBIL score evaluation: Increased consumer satisfaction.
2.	Users will get customized offers based on the data captured from their smartphones.
3.	Users will prefer using the proposed e-money due to the added purchasing powers



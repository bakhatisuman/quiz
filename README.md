# quiz

# To accomplish the quiz app , I have apply single activity based approach and used navigation component
# To get the data for quiz I have used below api : 
"OpenTrivia DB https://opentdb.com/api_config.php"
# To make app work offline I have used cache the data for 7 days. Due to insufficient of time I have not work with room database.
# As per the requirement I have made three fragments. One for splash screen. home screen for play quiz and the last for to show result.

# The below is google drive link for to download apk and some of the screen shots has been uploaded.
# Please see flow_app_picture folder for to see flow of application.
https://drive.google.com/drive/folders/1NkxOpdHay-7MJ2kBzpkZ7YB6l9cg0iV7?usp=sharing

# the below is Quiz App github link 
https://github.com/bakhatisuman/quiz

# About the data structure
The above api returns the data in Json Object. Among them For the quiz I have taken one of the object (""results"") which provides List of Questions 
along with correct answers and wrong options (List of String). I have made two models in the project to cast the data.



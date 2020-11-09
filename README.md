# Project 0 - Banking Application

**Description**
Leveraging Java 8, create an application that simulates simple banking transactions

**Requirements**
*	Build the application using Java 8 **DONE**
*	All interaction with the user should be done through the console using the `Scanner` class **DONE**
*	Customers of the bank should be able to register with a username and password, and apply to open an account. **DONE**
    * Stretch Goal: Customers should be able to apply for joint accounts **MAYBE**
*	Once the account is open, customers should be able to withdraw, deposit, and transfer funds between accounts **DONE**
    * All basic validation should be done, such as trying to input negative amounts, overdrawing from accounts etc. 
*	Employees of the bank should be able to view all of their customers information. This includes: **DONE**
    * Account information
    * Account balances
    * Personal information
*	Employees should be able to approve/deny open applications for accounts **DONE**
*	Bank admins should be able to view and edit all accounts. This includes: **DONE**
    * Approving/denying accounts **DONE**
    * withdrawing, depositing, transferring from all accounts **DONE**
    * canceling accounts **DONE**
*	Reasonable test coverage for the service layer is expected using JUnit. **DONE**
    * TDD is encouraged. **DONE**
*	Logging should be accomplished using Log4J **DONE**
    * All transactions should be logged **DONE**

* Create an SQL script that will create a user in an SQL database and a table schema for storing your bank users and account information. **DONE**
* Your database should include at least 1 stored procedure. **DONE**
* Have your bank application connect to your SQL database using JDBC and store all information that way.' **DONE**
* You should use the DAO design pattern for data connectivity. **DONE**

## Evaluation
The project will be evaluated out of 100 points split between two main catagories: 70 points for the functionality and design of your project and 30 points for the presentation of your project during the project showcase. The evaluation will be further subdivided as follows: 

**Project Score**(70 pts): 65
* 15 pts: Ability to persist meaningful data in the database and then retrieve it and display it to the console. **DONE**
* 15 pts: Ability to make withdrawals from, deposits to, and transfers between accounts. **DONE**
* 10 pts: Proper database schema achieving 3rd normal form. (E.g. Accounts have a proper relationship to their owning user.) **DONE**
* 10 pts: Login, register, update and logout functionality for users.  **DONE**
* 5 pts: Proper use of DAO design pattern.  **DONE**
* 5 pts: Different user roles with different levels of access implemented correctly. **DONE**
* 5 pts: Reasonable test coverage of the service layer and proper logging. **DONE**
* 5 pts: Appropriate validation for user inputs. **DONE**


**Presentation Score**(30 pts):
* 15 pts: Clear, concise and professional communication during the project presentation.
* 10 pts: Ability to communicate clear answers to fully address questions asked about the project. 
* 5 pts: Logical flow to the project presentation. 


## Frequently Asked Questions

1. When is the project due? 

    >A: The project will be due no earlier then Wednesday of Week 3. The exact due date will be set by QC as they schedule project presentations. 


2. Can I have an extension? 
    >A: No. While you are encouraged to continue to work on your projects past the date of the project presentation for your own learning benefit, extensions can not be accommodated. If for some reason you can not be present at the time of project presentations you will need to schedule a time BEFORE the due date to present your project. 

3. Is there a code freeze? 
    >A: It is recommended that you institute your own code freeze at least a day before the project presentations. However, this is a recommendation only; it will not be enforced. NOTE: The code that will be evaluated by your trainer will be the code you last pushed to your repository BEFORE the time set for project presentations. Code submitted while presentations are on-going will not be evaluated. 

4. What happens if I break my project that was mostly working right before the due date? 
    >A: As you should have been regularly pushing code to your repository you should be able to roll back to previously working version. If you have not regularly pushed your code and do not have a working commit to return to you will need to present the state of your application in its current form. 


5. Who will be evaluating the project? 
    >A: Your trainer will be the one providing the full evaluation of your projects. However, the QC team will also be present at presentations to ask questions about your project and consult with your trainer. 

6. Are we allowed to collaborate with others on our projects? 
    >A: Collaboration is encouraged. Hopefully you will work together to solve the problems presented in this project.However, you should still be ultimately designing the project yourself. Straight copy/pasting of another person's code is considered plagiarism. NOTE: Code provided in demos by your trainer is not subject to plagiarism concerns. Feel free to copy/paste and edit such code as necessary to suit your projects. 

7. What is a passing score? 
    >A: 75% is the base expected passing score.

8. If I fail the project will I be released from training?  
    >A: The project is a major component of the determination process for continued participation in training. Failure to present a project that at least has minimum functionality will lead to a release. 

9. Are there really no extensions? 
    >A: There are not.
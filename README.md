# XBCAD7319

Consultease Android App
Consultease is an Android application designed to streamline the process of service requests and management for both clients and administrators. The app supports two user roles: Client and Admin. Clients can request services and make payments, while Admins can manage user profiles, accept or deny service requests, approve payments, schedule meetings, and generate reports. The app also features biometric authentication for secure login and utilizes Firebase for user management and PayPal for payment processing.

Features:

Client Side:
•	User Authentication: Clients can register and log in to the app using their credentials or biometric authentication (fingerprint/face recognition).
•	Service Request: Clients can request various services such as Wi-Fi, ICT support, telephone services, etc.
•	Pricelist: A comprehensive list of available services along with their prices.
•	Payment Integration: Clients can make secure payments via PayPal for services they request.
•	Service Tracking: Clients can track the status of their service requests.

Admin Side:
•	User Authentication: Admins have their own registration and login, with biometric authentication.
•	Client Management: Admins can view the client list and access specific client profiles to manage personal information.
•	Service Request Management: Admins can review and accept or deny the service requests submitted by clients.
•	Payment Approval: Admins can approve or deny payments made by clients before the service is fulfilled.
•	Meeting Scheduling: Admins can schedule meetings with clients for further consultations or follow-ups.
•	Reports: Admins can generate reports related to client services, payments, and activities.

App Overview

The app is divided into two primary user roles:
1.	Client: A user who can request services, make payments, and track service status.
2.	Admin: A user who manages client requests, approves payments, schedules meetings, and generates reports.
   
Client Flow:
1.	Registration & Login: Clients register an account using their email or phone number, followed by login via credentials or biometric authentication.
2.	Service Request: Clients browse the available services (e.g., Wi-Fi, ICT support, etc.) and select a service they wish to request.
3.	Payment: Upon selecting a service, the client views the pricelist and makes a payment via PayPal.
4.	Payment Approval: The Admin will review and approve the payment before proceeding with the service request.
5.	Status Updates: Clients can track the status of their service requests and be notified of changes.
   
Admin Flow:
1.	Registration & Login: Admins can register and log in with their own credentials and biometric authentication.
2.	Client Management: Admins can view a list of clients and access detailed profiles with personal information.
3.	Service Request Approval: Admins can review and either approve or deny the service requests submitted by clients.
4.	Payment Approval: Admins review and approve payments made by clients before service fulfillment.
5.	Meeting Scheduling: Admins can schedule meetings with clients for further discussions or consultations.
6.	Reports: Admins can generate reports for service requests, payments, and other activities.
   
System Requirements

•	Android Version: 15.0 (VanillaIceCream) or higher
•	Internet Connection: Required for account authentication, service requests, and payment transactions.
Technical Details
•	Language: Kotlin for Android development
•	Authentication: Firebase Authentication with support for biometric authentication.
•	Cloud Firestore : storing user data and  service requests 
•	Payment Integration: PayPal API for processing payments.

How to Use

Client:

1.	Register/Login: Create an account by signing up or log in with existing credentials. Optionally, use biometric authentication for a faster login.
2.	Request Services: Browse the list of services and select the one you need (e.g., Wi-Fi setup, telephone support, etc.).
3.	Make Payment: After choosing a service, check the price list and proceed to payment using PayPal.
4.	Wait for Payment Approval: The Admin will approve the payment before processing the service request.
5.	Track Service: Monitor the status of your service request, including whether it is acceped, pending, or denied.
   
Admin:
1.	Register/Login: Admins create their own account and log in through the admin portal. Biometric authentication is available for added security.
2.	Manage Clients: View and search for client profiles, including personal details.
3.	Service Request Management: Review and either approve or deny client service requests based on availability and feasibility.
   
4.	Payment Approval: Admins will approve or deny client payments made through PayPal before fulfilling the service request.
6.	Schedule Meetings: Admins can schedule meetings with clients to discuss further details of their requests.
7.	Generate Reports: Admins can generate reports about client activity, payment status, and service request trends.

How the App Was Compiled
The Consultease Android App was developed using Kotlin as the primary programming language due to its efficiency and strong support for Android development. The development process included the following key components:
1.	Android Studio was used as the integrated development environment (IDE) to write, test, and compile the app.
2.	Firebase Authentication and Firebase Cloud Firestore were integrated for secure user authentication and data storage, respectively.
3.	PayPal API was used for secure payment integration to facilitate smooth transactions between clients and admins.
4.	The Biometric Authentication feature was enabled using Android's native biometric API to ensure secure and convenient login options for both clients and admins.
5.	The app follows the MVVM architecture pattern for better maintainability and scalability. This architecture helps in managing the UI, business logic, and data separately, which is essential for efficient app performance.
6.	Testing and debugging were carried out using Android's unit testing and UI testing tools to ensure the app is bug-free and user-friendly before deployment.


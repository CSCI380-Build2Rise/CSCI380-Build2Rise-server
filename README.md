# Build2Rise – Founder & Investor Networking Platform
Where innovation meets opportunity.
Build2Rise empowers founders and investors to discover each other through smart AI-driven matching, visual storytelling, and meaningful professional connections — all in one intuitive, social-inspired experience.

# 👥 Team Members & Contributions
Tanha:

Roles: Project Lead, Diagram Designer, UI Contributor, Front-End Contributor

Specific Contributions:

Helped manage the project and designated tasks

Contributed to the core app concept and feature ideation

Participated in app theme color research, brainstorming and final selection

Assisted in creating the ER diagram and fully developed the use case diagram for improved system clarity and planning

Contributed to designing Figma wireframes

Supported front-end development through component creation and UI refinement

Zarrin:

Roles: Documenter, UI Contributor, Front-End Developer, Back-end Developer

Specific contributions:

Contributed to the documentation of DF diagrams, Use Case diagram, and ER diagram

Contributed to the documentation of Figma design

Set up the environment within Android Studio

Contributed to the front-end development of our features

Contributed to the back-end development of our features

Expanded and updated the database schema to support new engagement features

Chelsea:

Roles: Documenter, Diagram Designer, UI Contributor, Front-End Developer

Specific contributions:

Contributed to the documentation of DF diagrams and ER diagram

Contributed in the creation of the ER diagram

Contributed to the front-end development of our features

Developed custom AI-generated visuals used across the welcome and login interfaces

Participated in app theme color research, brainstorming and final selection

Joseann:

Roles: Documenter, UI/UX Designer, Diagram Contributor

Specific contributions:

feature brainstorming informed by competitive analysis and UI/UX research of leading social and networking apps

Generated multiple logo concepts and visual assets for the mobile app

Created and maintained the Figma file and design libraries with prototyping

Set up the GitHub repository and initialized the core Android Studio environment

Contributed to the documentation of ERD and User Manual

Evelyn:

Roles: Documenter, UI/UX Designer, Front-End & Backend Contributor, Database Integration

Specific Contributions:

Contributed to the documentation of the system architecture, DF diagrams and ER diagrams.

Contributed to creating UI/UX figma design

Helped setup Supabase integration, authentication and image/video storage.

Contributed to the front-end development of our features

Structured backend/IntelliJ setup for team access and collaboration

Helped build the backend and integrate them with the front-end UI.

# 📌 About:
Build2Rise is a platform that combines the structured, professional features founders and angel investors need with the engaging, visual posting style people are used to on social media. It gives founders a dedicated space to showcase their startups and attract investor attention, while giving investors an easy way to discover, evaluate, and connect with promising projects/startups.

With features like tailored feeds, smart matching with AI, targeted search tools, and connection-based messaging, Build2Rise streamlines how founders and investors discover each other, interact, and build meaningful professional relationships, all within one intuitive platform.

#📱 User Guide:

# How to install and set up: 
Open the Build2Rise download link or APK file provided by the development team.

Open the app and select “Get Started” to sign up as a first time user. Returning users can log in to their existing account by clicking the “Log In” button.

Choose your user type: Founder or Investor.

Complete your profile to begin using the app.

## 🧭 How to use the app:
### Feed: 
After successfully signing up or logging in, users will land on the Feed, which is the first tab in the navigation bar. If the user is a founder, they can click the “+” icon on top of the feed to post about their startup via a picture, or a video, or a text post to the “Founders” section of the feed. If the user is an investor, they can click the “+” icon on top of the feed to post about looking for startups via a picture, or a video, or a text post to the “Investors” section of the feed. Both the founders and investors can view all the posts made in the Feed, but only the designated users can post to their designated feed, such as founders can only post on the “Founders” section of the feed and investors can only post on the “Investors” section of the feed. All users can like (by clicking the “Like” button) and comment (by clicking on the “Comment” button) on the posts in the Feed, as well as share those posts with their connections by clicking on the “Share” button. When a post is liked or commented on, the like count and the commented count increases and is shown to the users next to the said buttons, respectively.

### Search:
The second tab in the navigation bar is the “Search” tab. The “Search” page is divided into two sections, one is “Smart Match”, the other is “Search”.

In the “Smart Match” section, users will see that Google Gemini AI has presented a number of accounts for the founder or the investor, with other accounts that may have what they are looking for. AI will present to them the number of how many accounts a user is matched with based on deep profile analysis. The AI will also show users an average match score. It gives a 0-100% match score by analyzing industry, funding stage, location and team size capability. Matches above the 80% threshold represent an excellent match, while scores below 30% are filtered out. If AI said to the user that they smart matched with “2” users, smart match will show the profiles of those 2 users and will give the opportunity to connect and/or support those users. (Only users who are Investors will have the option to support Founders but every user, regardless of the type of users they are, can send a “Connection” request to other users).

In the “Search” section, users can filter their results by selecting the appropriate section before searching. For example, if someone is looking for FinTech investors, they can simply select Investors and type “FinTech”, only investors interested in FinTech will appear. Likewise, selecting Founders and searching “AI” will display only founders working in AI.

### Messages: 
The third tab in the navigation bar is the “Messages” tab. In there, investors and founders can message other investors and founders once they make connections with one another. When going through the feed or looking up users manually through the “Search” feature or finding users in the Smart Match, if a user sees someone they would like to connect with, they can send one another a request and upon the user who receives the request accepts it, they can send one another messages. Investors can connect and message both other investors and founders, and founders can do the same. Messaging is only available between users who have connected with one another.

### Profile: 
The last tab in the navigation bar is the “Profile” tab.

A founder’s profile page has their own Profile/Company/Product name. They also can click the “Connections” button to see all the people they are connected with which is done through sending and accepting connection requests between users. They also have an “About” tab under the “Connections” button where they can give a brief explanation of their startup and its goal. All of a Founder’s posts made and posted to the feed can be accessed under the “Posts” section in their profile. A founder’s one and only project can be found posted under the “Projects” section. A founder can make multiple regular posts, which can help them get the traction they need and find someone who will invest in them. Founders are limited to post one project under one account only.

An investor’s profile page has their own Profile/Company name. They also can click the “Connections” button to see all the people they are connected with which is done through sending and accepting connection requests between users. All of an Investor’s posts made and posted to the feed can be accessed under the “Posts” section in their profile. The projects an investor is supporting can be found under the “Supported Projects” section. An investor can make multiple posts, which can help them find companies/products they can invest into. An investor can support more than one project.

### Log out: 
both user types, investors and founders can log out of their accounts by clicking the log out button on the top right corner of their own profile page.

# 🧪 Testing Strategies and Test Cases:
To ensure that the Build2Rise platform works reliably for both Founder and Investors, several types of testing strategies were used throughout development. These strategies have helped validate that the app’s core features, account creation, posting updates, showcasing projects and supporting startups, function correctly and provide a smooth user experience.

## Test Case 1: AI Smart Match - Founder to Investor
Description: This test checks if the AI Smart Match feature correctly analyzes a founder’s profile and displays suitable investor profiles.

Expected Behavior:

Recommended investors should align with the founder’s industry

Funding stage Preferences should match (Seed, Series A, etc.)

Geographic or sector preferences should influence recommendations.
<img width="347" height="772" alt="image" src="https://github.com/user-attachments/assets/51f2ba33-25ed-4370-b6ae-326a0c58896b" />


## Test Case 2: AI Smart Match - Investor to Founder
Description: This test checks if the AI Smart Match feature correctly analyzes an investor’s preferences and displays suitable founder’s profiles.

Expected Behavior:

Recommended startups that align with the investor’s industry and region.

## Test Case 3: Add a post to their feed
Description: Verify a founder can add a post to their feed:

Steps:

Select on the Feed tab in the navigation bar

Click on the "+" icon on the top right corner

Add a caption

Tap “post.”

Wait for an UI update

Expected Results:

## Test Case 4: Investor Supports a Founder’s Project
Description: Check that investor can support a project

Steps:

Select the Search tab in the navigation bar

Review the Smart Matches or Search for a founder by name or based on startup information

Click on the support button

Wait for confirmation message or UI Update

Expected Results:

In the Investor’s Profile, the supported projects would be updated

## Test Case 5: Connect with another user
Description: Ensure users can send a connection request to another user

Steps:

Select on the Search tab in the navigation bar

Review the profiles of other users

Tap on the “Connect” button

Wait for confirmation message or UI Update

Expected Results:

The connection request is sent successfully

This request can be viewed from their profile by clicking the “Connections” text

The target user received a request in “My Connections” with the option to Accept or Decline the request.

## Test Case 6: Login & Load Feed
Description:

Ensure Users can log in and view their personalized dashboard

Steps:

Open the app

Select the “log in” text at the button of the screen

Enter your account details

Email: zislam04@nyit.edu

Password:

Click on the log-in button

Expected Results:

## Test Case 6: Signup & Load Feed
Description:

Ensure new users can sign in and view their personalized dashboard

Steps:

Open the app

Click on the Sign up button

Select your user type: Founder or Investor:

For Investor:

Investors enter details about their investment style and preferences

Then select the “Continue” button

Lastly confirm their account login credentials for future

Then click on the “Create Account” button

Expected Results:

The user receives confirmation that their account has been created successfully.

Suggested next steps appear, such as completing profile details or exploring the feed.

Users may proceed directly to the Feed.

For Founder:

Founders must provide basic company and startup-related information

Then select the “Continue” button

Lastly confirm their account login credentials for future

Then click on the “Create Account” button

Expected Results:

The user receives confirmation that their account has been created successfully.

Suggested next steps appear, such as completing profile details or exploring the feed.

Users may proceed directly to the Feed.

# 🛠 Technology Stack:
Frontend: The application was built as a native Android application using Kotlin and the Jetpack Compose for modern UI development. This helps build the dynamic interfaces that are essential for the pitch feed, real time community interactions, and video content.

Backend: The server-side architecture uses Spring Boot framework which is implemented in Kotlin. Core services include user management for multiple user types (founders and investors), content management for ranking pitch videos based on engagement metrics, real-time communication infrastructure for messaging and video calling, and automated scheduling systems for investor meetings. The security for Spring Boot’s features can be enhanced with JWT authentication, rate limiter to protect user data and ensure secure founder-investor interactions through the platform. Included RESTful api architecture such as controllers(api endpoints), services (business logic), repositories (data access) and DTOs (data transfer objects).

AI: Leverages Google Gemini AI, utilizes Google's generative language API to perform semantic analysis of user profiles, to understand contextual relationships.

Database: Supabase is where the data will be stored, as it provides a PostgreSQL database with real-time capabilities, authentication, and file storage that supports complex data relationships needed for social interactions. The database will store user profiles for both user types, startup information with pitch video, social interaction data (likes, comments, engagement metrics).

# ⚙️Setup Guide: 
To set up the Build2Rise development environment, begin by cloning the repository, CSCI380-Build2Rise-server, and opening the backend folder in IntelliJ IDEA. Inside the backend, a new application.properties file must be created manually within the src/main/resources directory, since this file contains private keys and credentials that are intentionally excluded from version control. The backend server can then be run.

For the frontend setup, open the frontend in Android Studio and run the app once the backend is still running. Together, these steps prepare both the server and client environments for development or testing. The production environment follows the same process but requires hosting the Spring Boot backend on a cloud server and updating the mobile app’s base URL to match the backend.

# ✨ Features and Technical Implementation: 
The Build2Rise app has 2 types of users: Founders and Investors. After signing up or logging in, the users will be exposed to the following features of the app:

## Feed: 
The Feed page is divided into two sections, “Founders” and “Investors”. If users (either a founder or an investor), clicks on the “Founders” section, they will be able to see all the founders’ posts that are on there and they can interact with those posts by liking or commenting. On the other hand, if a user clicks on the “Investors” section, they will see all the posts made by investors and also interact with those posts by liking and commenting.

## Search:
The Search page is divided into two sections: Smart Match and Search. The default section for users when in the Search page is Smart Match. Build2Rise’s smart matching system uses Gemini AI to intelligently recommend the best match between an investor with a founder and vice versa. Provides a 0-100% match score by analyzing industry, funding stage, location and team size capability. Matches above the 80% threshold represent an excellent match, while scores below 30% are filtered out. When a user clicks on the Search tab, that page will show two sections: Founders and Investors. Users can filter their results by selecting the appropriate section before searching. For example, if someone is looking for FinTech investors, they simply select Investors and type “FinTech”, only investors interested in FinTech will appear. Likewise, selecting Founders and searching “AI” will display only founders working in AI. This separation makes the search process highly efficient, focused, and relevant to the user’s needs.

## Messages:
Investors and founders can message others investors and founders once they make connections with one another. When going through the feed or looking up users manually through the “Search” feature, if a user sees someone they would like to connect with, they can send one another a request and upon the user who receives the request accepts it, they can send one another messages. Investors can connect and message both other investors and founders, and founders can do the same, allowing seamless communication across the entire community. Messaging is only available between users who have connected with one another.

## Founder’s Profile: 
A founder’s profile page has their own Profile/Company/Product name, how many connections they have, all of their posts under the “Posts” section and their one and only project posted under the “Projects” section. A founder can make multiple posts, which can help them get the traction they need and find someone who will invest in them. Founders are limited to post one project under one account only because this maintains the seriousness and credibility of each founders’ profile, ensures founders present their primary venture with focus and dedication, and lets investors easily understand what a founder is building.

## Investor’s Profile: 
An investor’s profile page has their own Profile/Company name, how many connections they have, all of their posts under the “Posts” section and the projects they are supporting under the “Supported Projects” section. An investor can make multiple posts, which can help them find companies/products they can invest into. An investor can support more than one project because supporting multiple projects increases the chances for founders to be discovered and funded.

## Log out: 
All users have the option to log out by clicking the log out account above their profile page on the top right.

# 📦 Packages and APIs:
The Build2Rise app uses a combination of Android libraries and backend technologies to create a secure, efficient, and responsive platform. On the frontend, Retrofit is used as the primary HTTP client due to its reliability and seamless integration with Kotlin coroutines, while the OkHttp Interceptor enables logging for debugging network calls. Gson is used for JSON parsing, ensuring smooth communication with the backend. For secure local storage, DataStore stores authentication tokens and user information. The UI navigation is handled with Navigation Compose, providing a clean approach to screen transitions. Media handling is supported by Coil for image loading and ExoPlayer for pitch video playback, while ViewModel and Kotlin Coroutines manage application state and asynchronous operations efficiently.

On the backend, Build2Rise uses Spring Web to define RESTful API endpoints, Gemini API for AI-powered semantic matching and Spring Security to enforce authentication and authorization policies. Spring Data JPA serves as the ORM layer, simplifying database interactions with PostgreSQL, while the PostgreSQL Driver manages direct communication with the database. Authentication is implemented using JJWT, which supports secure token-based authorization across the app. Commons IO helps handle file uploads and media management, and OkHttp is used server-side for interacting with external storage services.

These technologies were chosen so that Jetpack Compose enables UI development with rapid iteration, and Retrofit provides a reliable HTTP layer ideal for mobile apps. Additionally, Supabase/PostgreSQL is used as a relational database for social interactions and media-driven features. Spring Boot provides a backend framework, and JWT authentication ensures secure communication. The Gemini API was used to perform semantic analysis by generating high quality text embeddings to compute AI-generated Smart Match.

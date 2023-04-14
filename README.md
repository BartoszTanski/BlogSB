# Backend with spring boot for Blog application (still in development)

### This is a spring boot app that provides REST API endpoints, that enable CRUD operations on posts and comments. Backend is connected to MongoDB 

***
## Frontend application is deployed on azure web services and accesible (_[here](https://bartosztanski.azurewebsites.net "bartosztanski.azurewebsites.net")_)
## Backend application is also deployed on azure web services and accesible (_[here](https://blogbartosz.azurewebsites.net "blogbartosz.azurewebsites.net")_)
#### Backend endpoints:
- https://blogbartosz.azurewebsites.net/api/v1/posts (GET) Returns list of all posts in database
- https://blogbartosz.azurewebsites.net/api/v1/posts (POST) Adds new post to database
- https://blogbartosz.azurewebsites.net/api/v1/post/{postId} (GET) Returns single post with given id
- https://blogbartosz.azurewebsites.net/api/v1/post/{postId} (PUT) Modifies post with given id
- https://blogbartosz.azurewebsites.net/api/v1/post/{postId}/comments (GET) Returns comments to post with given id
- https://blogbartosz.azurewebsites.net/api/v1/post/{postId}/comments (POST) Adds new comment to post with given id
- https://blogbartosz.azurewebsites.net/api/v1/post/{postId}/{commentId} (DELETE) Deletes comment with given id from post
***

To perform actions other than GET use REST CLIENT ex.(Postman, Insomnia)

# Spring Boot API Passthrough

**A Jahnel Group sample project demonstrating configuring basic OAuth2.0 security for an authorization and resource server**

The application contains:
1. An OAuth2.0 authorization server able to create sessionless JWT tokens
2. An OAuth2.0 resource server that can decode JWT tokens to verify users

### Running the Service
````
gradle bootRun
````

Once its running you can you use the following flows to get an access token.

##### Client Credentials flow
````
curl -XPOST --user in-memory:super_secret_password localhost:8080/oauth/token?grant_type=client_credentials
````

The token will be found in the returned JSON object under access_token

#### Testing the Resource server

The application contains one endpoint under `/api/principal` that simply returns who is currently authenticated. 
Use the access token ($access_token) found in the above flows to access it. Attempting to access it without the token should return a HTTP Error of 401.

##### Unauthorized Access
````
curl localhost:8080/api/principal
````

#### Authorized Access
````
 curl -H "Authorization: Bearer $access_token" localhost:8080/api/principal
````

### Other notes
I have included the jks file here for ease of use. In an actual project, 
please generate your own and DO NOT commit to history nor commit the other client secrets.

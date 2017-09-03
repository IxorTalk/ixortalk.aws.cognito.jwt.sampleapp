# Introduction

This is a sample Spring Boot application to show you how easy it is to do authentication / authorization using Amazon Cognito.
By using the IxorTalk Cognito JWT Library, you'll be able to authenticate users and have them access your APIs, but also use
Cognito roles to do authorization.

In order to get this sample up and running you'll need to perform a couple of steps :

- Setup your Cognito components (user pools, identity pools, users).
- Setup roles and users
- Configure the sample app with the correct cognito params
- Run the sample

Lets get started :

# CloudFormation setup

We provide a [CloudFormation template](https://github.com/IxorTalk/ixortalk.aws.cognito.cloudformation) to set this up in case you haven't setup Cognito yet.

## Role and User creation

Define your user pool ID in a variable:
```
USER_POOL_ID=eu-central-1_xxxxxxxxx
```

Create 2 roles:
```
aws cognito-idp create-group --group-name Admin --user-pool-id ${USER_POOL_ID}
aws cognito-idp create-group --group-name User --user-pool-id ${USER_POOL_ID}
```

Create 2 users and add them to the correct group:
```
aws cognito-idp admin-create-user --user-pool-id ${USER_POOL_ID} --username admin --temporary-password SecretPwd_123 --user-attributes "Name=email,Value=ddewaele@gmail.com" "Name=email_verified,Value=true"
aws cognito-idp admin-add-user-to-group --user-pool-id ${USER_POOL_ID} --username admin  --group-name Admin
aws cognito-idp admin-add-user-to-group --user-pool-id ${USER_POOL_ID} --username admin  --group-name User


aws cognito-idp admin-create-user --user-pool-id ${USER_POOL_ID} --username user --temporary-password SecretPwd_123 --user-attributes "Name=email,Value=davy.dewaele@ixor.be" "Name=email_verified,Value=true"
aws cognito-idp admin-add-user-to-group --user-pool-id ${USER_POOL_ID} --username user --group-name User

```

## Sample App Configuration

In your application yml, you'll need to provide the following cognito identifiers:

```
com:
  ixortalk:
    security:
        jwt:
          aws:
            userPoolId: "eu-central-1_xxxxxxxxx"
            identityPoolId: "eu-central-1:xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
            region: "eu-central-1"
```            


## Executing a call 

To execute REST calls you'll need to provide a valid JWT token in the authorization header.

We provide a [Simple AWS Cognito client for retrieving JWT tokens from Cognito credentials](https://github.com/IxorTalk/ixortalk.aws.cognito.token.retrieval)

It can be installed as a global node module and is able to retun TOKENS like this:

```
cognito-login -e dev -u admin -p SecretPwd_123

  _____             _______    _ _             __          _______       _____                  _ _                 ___          _________
 |_   _|           |__   __|  | | |           /\ \        / / ____|     / ____|                (_) |               | \ \        / /__   __|
   | | __  _____  _ __| | __ _| | | __       /  \ \  /\  / / (___      | |     ___   __ _ _ __  _| |_ ___          | |\ \  /\  / /   | |
   | | \ \/ / _ \| '__| |/ _` | | |/ /      / /\ \ \/  \/ / \___ \     | |    / _ \ / _` | '_ \| | __/ _ \     _   | | \ \/  \/ /    | |
  _| |_ >  < (_) | |  | | (_| | |   <      / ____ \  /\  /  ____) |    | |___| (_) | (_| | | | | | || (_) |   | |__| |  \  /\  /     | |
 |_____/_/\_\___/|_|  |_|\__,_|_|_|\_\    /_/    \_\/  \/  |_____/      \_____\___/ \__, |_| |_|_|\__\___/     \____/    \/  \/      |_|
                                                                                     __/ |
                                                                                    |___/

awsEnvironment : 
 { UserPoolId: 'eu-central-1_0VEinu0cg',
  ClientId: 'm6uu1npo6fpt4oeitb6a7475m',
  IdentityPoolId: 'eu-central-1:68f40db0-62e8-44f2-9a29-7b4658aa747b',
  AWSRegion: 'eu-central-1' }

user : 
 admin

credential : 
 { Password: 'SecretPwd_123' }


export TOKEN=eyJraWQiOiIxRWpWRXlNR3BYXC9jV0J5eTVtYlFEWW0wR0lhTjF2eVlYT1pLNkZtSTJ3Yz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiNDgwZTQxNi1kYmNhLTQ4NjktYTU4OC1mZjdjODJmY2ZiYWEiLCJhdWQiOiJtNnV1MW5wbzZmcHQ0b2VpdGI2YTc0NzVtIiwiY29nbml0bzpncm91cHMiOlsiQWRtaW4iXSwiZW1haWxfdmVyaWZpZWQiOnRydWUsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNTA0NDQ5NjkyLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2V1LWNlbnRyYWwtMV8wVkVpbnUwY2ciLCJjb2duaXRvOnVzZXJuYW1lIjoiYWRtaW4iLCJleHAiOjE1MDQ0NTMyOTIsImlhdCI6MTUwNDQ0OTY5MiwiZW1haWwiOiJkZGV3YWVsZUBnbWFpbC5jb20ifQ.EFVf4oVyAdJ7Zs_763OG69YsX8ss13Fy1MjzcvWfAFOlbP4F0RvqTolOif8szaPhG5FFf-TlGkfaEX3gulJfXU8cS-7WIf8BmQ9NnkUNmXxjmPo5wmCFzaj__rNqWM9D22vaaTCBfffmNI-EQ6PL0nTIzcSGNB7rr59VRYQ1B-5zoKDNNSwdtiBjxCppjLGs1-C_7tnW6EAenq2DUSlRYIt-kzQR7OjhkHsOI20pyHlv_SY5hCMHSHI6jdEuqqtqUbKG8JlPmx9WC9SEencEIlB0KMjVZl3qGi8wlqvGn8GrSwunkGSUNnpoyL97ohdIH2W1Di8quURwdqO0qwa2Ow
```

Now that you have the token, you can execute API calls :

### Returning a principal

```
curl -v -H "Authorization: $TOKEN" http://localhost:8080/api/me | jq
```

You should see a response like this :

```
{
  "authorities": [
    {
      "authority": "ROLE_ADMIN"
    }
  ],
  "details": null,
  "authenticated": true,
  "principal": {
    "password": "",
    "username": "admin",
    "authorities": [
      {
        "authority": "ROLE_ADMIN"
      }
    ],
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true,
    "enabled": true
  },
  "jwtClaimsSet": {
    "claims": {
      "sub": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
      "aud": [
        "xxxxxxxxxxxxxxxxxxx"
      ],
      "cognito:groups": [
        "Admin"
      ],
      "email_verified": true,
      "token_use": "id",
      "auth_time": 1504459590,
      "iss": "https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_xxxxxxx",
      "cognito:username": "admin",
      "exp": 1504463190000,
      "iat": 1504459590000,
      "email": "ddewaele@gmail.com"
    },
    "jwtid": null,
    "subject": "b480e416-dbca-4869-a588-ff7c82fcfbaa",
    "audience": [
      "m6uu1npo6fpt4oeitb6a7475m"
    ],
    "expirationTime": 1504463190000,
    "notBeforeTime": null,
    "issueTime": 1504459590000,
    "issuer": "https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_xxxxxxx"
  },
  "credentials": null,
  "name": "admin"
}

```

To demonstrate the authorization part, the API also exposes 2 additional endpoints


### Endpoint that requires the ADMIN role (admin user)
```
curl -v -H "Authorization: $TOKEN" http://localhost:8080/api/superSecure | jq
```

### Endpoint that requires the USER role (admin user or user user)
```
curl -v -H "Authorization: $TOKEN" http://localhost:8080/api/secure | jq
```

# License

```
The MIT License (MIT)

Copyright (c) 2016-present IxorTalk CVBA

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```




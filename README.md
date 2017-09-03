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

We provide a CloudFormation template to set this up in case you haven't setup Cognito yet.

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




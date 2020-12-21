[![Build Status](https://travis-ci.com/notesmen/geonotes-server.svg?token=UBkXChuz14opdpFfM5qT&branch=master)](https://travis-ci.com/notesmen/geonotes-server)

# About

It is backend part of GeoNotes project which is used for storing notes in the cloud and 
keep them available for you from everywhere. 

Written in Kotlin using Spring Framework.

# Deployment

0. Set up mongodb server

1. Set up parameters at `application.properties` 

Options that have to be specified at file `src/main/resources/application.properties`:

<details>
<pre>
spring.data.mongodb.host
spring.data.mongodb.port
spring.data.mongodb.database
spring.data.mongodb.authentication-database
spring.data.mongodb.username
spring.data.mongodb.password 
jwt.token-validity-duration # in hours
jwt.encoded-secret # your secret key, base64 encoded

\# default values
api.min-username-length=3
api.max-username-length=20
api.min-password-length=4
api.max-password-length=24
api.max-notes-per-request=30
api.max-title-length=20
api.max-text-length=1000
api.max-tags-total-length=1000
</pre>
</details>

2. Build docker image:
<pre>./gradlew bootBuildImage --imageName=geonotes/server</pre>

3. Run image on server

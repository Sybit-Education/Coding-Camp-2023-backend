# Sygotchi - Backend



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.sybit.dev/sybit/education/sybit-coding-camp/sybit-coding-camp-2023/sygotchi-backend.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.sybit.dev/sybit/education/sybit-coding-camp/sybit-coding-camp-2023/sygotchi-backend/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

## Indroduction

This is the backend of the Sygotchi project. It is written in Java using the Spring Boot framework. The project is built using Maven.

## Architecture

The project is split into three layers: 
    The controller layer, 
    the service layer 
    and the repository layer. 
    The controller layer is responsible for handling the requests and responses. The service layer is responsible for the business logic. The repository layer is responsible for the database access.


## API Documentation

The API documentation is available at [http://localhost:8080/docs](http://localhost:8080/docs).

## Authentication and Authorization

The authentication is done using JWT. The JWT is sent in the Authorization header of the request. The JWT is signed using a secret. The secret is stored in the environment variable JWT_SECRET. The JWT contains the user id of the user. The user id is used to identify the user. The JWT is valid for 30 days. The JWT is refreshed every time the user sends a request
too the backend. The JWT is refreshed by sending a new JWT in the Authorization header of the response. The JWT is refreshed by the JwtFilter.


## Database

The database is a In memory database. The database is automatically migrated using Flyway. The migrations are located in the src/main/resources/db/migration folder.


## Basics of Java

### Variables


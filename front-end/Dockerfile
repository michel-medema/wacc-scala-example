# Based on https://medium.com/@wkrzywiec/build-and-run-angular-application-in-a-docker-container-b65dbbc50be8.

### STAGE 1: Build ###
FROM node:14.10-alpine AS build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

### STAGE 2: Run ###
FROM nginx:1.19.2-alpine
#COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/front-end /usr/share/nginx/html

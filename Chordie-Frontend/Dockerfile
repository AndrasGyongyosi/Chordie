#FROM node:alpine

#ENV PORT=4200
#ENV NODE_OPTIONS=--openssl-legacy-provider

#WORKDIR /var/www
#COPY package.json package-lock.json ./
#RUN npm install -f

#COPY . .

#ENTRYPOINT ["npm", "start"]

#EXPOSE $PORT

# Stage 1: Compile and Build angular codebase

# Use official node image as the base image
FROM node:latest as build

ENV NODE_OPTIONS=--openssl-legacy-provider

# Set the working directory
WORKDIR /usr/local/app

# Add the source code to app
COPY ./ /usr/local/app/

# Install all the dependencies
RUN npm install -f

# Generate the build of the application
RUN npm run build


# Stage 2: Serve app with nginx server

# Use official nginx image as the base image
FROM nginx:latest

# Copy the build output to replace the default nginx contents.
COPY --from=build /usr/local/app/chordie-frontend /usr/share/nginx/html

# Expose port 4200
EXPOSE 4200
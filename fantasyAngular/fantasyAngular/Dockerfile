# Stage 1: Build Angular app
FROM node:16-alpine AS builder

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install --force

# Copy the rest of the application code to the working directory
COPY . .

# Build the Angular app
RUN npm run build

# Stage 2: Create a smaller image for runtime
FROM nginx:alpine

# Copy the built app from the builder stage to the nginx public directory
COPY --from=builder /usr/src/app/dist/ /usr/share/nginx/html

COPY default.conf /etc/nginx/conf.d/default.conf

# Expose the port the app runs on
EXPOSE 80

# Command to run the application
CMD ["nginx", "-g", "daemon off;"]
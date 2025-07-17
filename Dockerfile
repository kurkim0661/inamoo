# Use official Node.js LTS image
FROM node:20

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package.json yarn.lock ./
RUN yarn install

# Copy the rest of the app
COPY . .

# Expose the dev server port
EXPOSE 5173

# Default command (can be overridden by docker-compose)
CMD ["yarn", "dev", "--host", "0.0.0.0"]

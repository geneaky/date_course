FROM node:alpine
WORKDIR '/app'
COPY package*.json .
RUN npm install 
COPY . . 
CMD ["npm","run","start"]

# FROM nginx
# COPY --from=builder /app/build /usr/share/nginx/html
# EXPOSE 3000
# CMD ["nginx","-g","daemon off;"]

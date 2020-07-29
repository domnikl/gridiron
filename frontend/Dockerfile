FROM node:14.5 AS builder
WORKDIR /app
ADD . /app
RUN npm install --only=prod && npm run build

FROM nginx:1.19
COPY --from=builder /app/dist /usr/share/nginx/html

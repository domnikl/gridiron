FROM node:14.5 AS builder
WORKDIR /app
ADD . /app
RUN npm install lint build

FROM nginx:1.19
COPY --from=builder /app /usr/share/nginx/html

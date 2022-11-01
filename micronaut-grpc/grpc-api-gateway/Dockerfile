FROM busybox:stable-musl as busybox
FROM nginx:1.23.2-alpine

COPY --from=busybox /bin/ping /bin/ping
COPY --from=busybox /bin/find /bin/find

COPY nginx.conf /etc/nginx/nginx.conf.template
COPY entrypoint.sh /entrypoint.sh
RUN chmod 755 entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

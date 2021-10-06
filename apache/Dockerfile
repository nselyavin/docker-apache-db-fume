# На всякий случай напоминаю, что Dockerfile всегда должен начинаться с импорта базового образа.
# Для этого используется ключевое слово 'FROM'.
# Здесь нам нужно импортировать образ PHP + Apache (с DockerHub).
# В результате мы, в качестве имени образа, указываем 'php: -apache', а в качестве версии php - '7.2'

FROM php:7.2-apache
RUN apt-get update

RUN apt-get update -y && apt-get install -y sendmail libpng-dev
RUN apt-get update && \
    apt-get install -y \
    zlib1g-dev 

RUN docker-php-ext-install mbstring
RUN docker-php-ext-install zip
RUN docker-php-ext-install gd

# RUN chown -R www-data:www-data /var/www
# RUN usermod -u 1000 www-data

RUN docker-php-ext-install mysqli

WORKDIR /var/www/html

# ENV PORT 9000
# EXPOSE 9000
# ENTRYPOINT []
# CMD sed -i "s/80/$PORT/g" /etc/apache2/sites-available/000-default.conf /etc/apache2/ports.conf && docker-php-entrypoint apache2-foreground
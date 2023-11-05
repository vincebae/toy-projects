#!/usr/bin/env python
from flask import Flask, render_template, abort
from views.forms import LoginForm

app = Flask(__name__)
app.config['SECRET_KEY'] = 'asdf;2325jkldgio3445gndjkswerthiogf'
PORT = 8080


@app.route("/")
def home():
    return render_template("home.html")


@app.route("/login", methods=["GET", "POST"])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        print("Email: ", form.email.data)
        print("Password: ", form.password.data)
        if (form.email.data == "vincebae@gmail.com"
                and form.password.data == "1234"):
            return render_template(
                "login.html",
                form=form,
                message="Successfully logged in")
        else:
            return render_template(
                "login.html",
                form=form,
                message="Login failed")
    elif form.errors:
        print(form.errors.items())

    return render_template("login.html", form=form)


@app.route("/square/<int:number>")
def show_square(number):
    return f"Square of {number} is {number * number}"


@app.route("/add/<int:number1>/<int:number2>")
def show_add(number1, number2):
    return f"{number1} + {number2} = {number1 + number2}"


@app.route("/failure")
def failure():
    abort(404, description="It's failure page, so what did you expect?")


if __name__ == "__main__":
    app.run(debug=True, port=PORT)

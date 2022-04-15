import os

from flask import *

app = Flask(__name__)


@app.route("/", methods=["POST"])
def home():
    img = Image.open(request.files['file'])
    point = request.args.get('point')
    info = request.args.get('info')
    park = request.args.get('park')
    url = request.args.get('url')
    location_longitude = request.args.get('location_longitude')
    location_width = request.args.get('location_width')

@app.route("/get/<name>")
def get_image(name):
    try:
        return send_from_directory(os.path.abspath(os.curdir), name, as_attachment=True)
    except FileNotFoundError:
        abort(404)


if __name__ == "__main__":
    app.run(host='0.0.0.0')

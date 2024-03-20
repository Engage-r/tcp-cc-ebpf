from flask import Flask, request, jsonify
import subprocess  # for executing shell commands (use with caution)

app = Flask(__name__)

# Replace with your desired port number
PORT = 5000


@app.route("/", methods=["POST"])
def handle_post():
  # Access data from the request (assuming JSON format)
  data = request.get_json()

  # Check if 'cc' key exists and data is present
  if data and 'cc' in data:
    # Extract the string value from the 'cc' key with basic sanitization
    cc_value = data['cc'].strip()  # Remove leading/trailing whitespaces

    # Sanitize user input (important to prevent code injection)
    # Replace with a more robust sanitization approach based on your makefile logic
    # (e.g., using regular expressions or escaping special characters)
    sanitized_cc_value = cc_value.replace(";", "\\;")  # Example: escape semicolons

    # Construct the make command with sanitized data (use caution)
    make_command1 = f"make change-map CCString='{sanitized_cc_value}'"
    make_command2 = f"make run-user CCString='{sanitized_cc_value}'"

    # Execute make commands using subprocess (cautiously and securely)
    try:
      subprocess.run(make_command1, shell=True, check=True)
      subprocess.run(make_command2, shell=True, check=True)
      message = f"Successfully executed make commands with cc: {sanitized_cc_value}"
    except subprocess.CalledProcessError as e:
      message = f"Error running make commands: {e}"
  else:
    message = "No data or 'cc' key not found in request."

  # Return a JSON response
  return jsonify({"message": message}), 200

if __name__ == "__main__":
  app.run(host="0.0.0.0", port=PORT)

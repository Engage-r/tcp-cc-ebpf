from flask import Flask, request, jsonify
import subprocess
import psutil
import time


app = Flask(__name__)
PORT = 5000


@app.route("/cc", methods=["POST"])
def handle_post():
    data = request.get_json()

    if data and "tcpccAlgorithm" in data:
        cc_value = data["tcpccAlgorithm"].strip()

        sanitized_cc_value = cc_value.replace(";", "\\;")

        # Construct the make command with sanitized data
        make_command1 = f"make change-map CCString='{sanitized_cc_value}'"
        make_command2 = f"make run-user CCString='{sanitized_cc_value}'"

        # Execute make commands using subprocess
        try:
            subprocess.run(make_command1, shell=True, check=True)
            subprocess.run(make_command2, shell=True, check=True)
            message = (
                f"Successfully executed make commands with cc: {sanitized_cc_value}"
            )
        except subprocess.CalledProcessError as e:
            message = f"Error running make commands: {e}"
    else:
        message = "No data or 'tcpccAlgorithm' key not found in request."

    return jsonify({"message": message}), 200


@app.route("/getthroughput", methods=["POST"])
def get_throughput():
    interface = "wlp2s0"
    net_io = psutil.net_io_counters(pernic=True)
    if interface in net_io:
        bytes_sent_prev = net_io[interface].bytes_sent
        time.sleep(1)
        net_io = psutil.net_io_counters(pernic=True)
        bytes_sent_now = net_io[interface].bytes_sent
        throughput = bytes_sent_now - bytes_sent_prev
        return jsonify(
            {
                "throughput": throughput / 128,
                "date": time.strftime("%Y-%m-%d"),
                "time": time.strftime("%H:%M:%S"),
            }
        )
    else:
        return jsonify({"error": "Interface not found"}), 404


@app.route("/throughput", methods=["POST"])
def set_throughput():
    data = request.get_json()
    if data and "throughput" in data:
        limit = data["throughput"]
        command = f"sudo tc qdisc add dev wlp2s0 root tbf rate {limit}kbit burst 2000kbit latency 100ms"
        try:
            subprocess.run("sudo tc qdisc del dev wlp2s0 root", shell=True, check=False)
            subprocess.run(command, shell=True, check=True)
            message = f"Successfully set throughput limit to {limit} kbps"
        except subprocess.CalledProcessError as e:
            message = f"Error setting throughput limit: {e}"
    else:
        message = "No data or 'limit' key not found in request."
    return jsonify({"message": message}), 200


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=PORT)

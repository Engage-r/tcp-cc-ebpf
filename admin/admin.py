import argparse
import requests


def add_entry(ip, port):
    with open("entries.txt", "a") as file:
        file.write(f"{ip}:{port}\n")


def get_entry(idx):
    with open("entries.txt", "r") as file:
        entries = file.readlines()
        try:
            entry = entries[idx - 1]
            ip, port = entry.strip().split(":")
            return ip, port
        except IndexError:
            print("Invalid entry index.")
            return None, None


def list_entries():
    with open("entries.txt", "r") as file:
        entries = file.readlines()
        for idx, entry in enumerate(entries, start=1):
            ip, port = entry.strip().split(":")
            print(f"{idx}. {ip}:{port}")


def make_post_request(idx):
    try:
        ip, port = get_entry(idx)
    except IndexError:
        print("Invalid entry index.")
        return

    url = f"http://{ip}:{port}"
    json_data = input("Enter JSON body for the POST request: ")
    try:
        response = requests.post(url, data=json_data)
        print(f"POST request to {url} returned status code {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"An error occurred: {e}")


def main():
    parser = argparse.ArgumentParser(description="IP and Port Manager")
    parser.add_argument(
        "--add", metavar=("IP", "PORT"), nargs=2, help="Add a new IP and port"
    )
    parser.add_argument(
        "--list", action="store_true", help="List all stored IP and port entries"
    )
    parser.add_argument(
        "--post",
        metavar="INDEX",
        type=int,
        help="Make a POST request to the IP and port at the specified index",
    )
    args = parser.parse_args()

    if args.add:
        add_entry(args.add[0], args.add[1])
        print("Entry added successfully.")
    elif args.list:
        list_entries()
    elif args.post is not None:
        make_post_request(args.post)
    else:
        parser.print_help()


if __name__ == "__main__":
    main()

sudo iptables -A OUTPUT -p tcp --dport 12345 -j DROP
sleep 5
sudo iptables -D OUTPUT -p tcp --dport 12345 -j DROP
# Instant Messaging System using RabbitMQ & Java  

This project is a **high-performance, distributed instant messaging system** built in **Java**. It utilizes **RabbitMQ** as a message broker, implementing a **publish/subscribe (pub/sub) architecture** to enable real-time communication between users. Messages are **serialized and deserialized using Google Protocol Buffers**, ensuring efficient data transmission. Additionally, the system supports **group conversations, file sharing, and load balancing** with a **RabbitMQ cluster deployed on AWS EC2**, using a **Network Load Balancer (NLB) for scalability and fault tolerance**.  

---

## Key Features  

✅ **Real-time messaging** using RabbitMQ’s message broker  
✅ **Efficient message serialization** with Google Protocol Buffers  
✅ **Group chat support** with RabbitMQ **Exchanges**  
✅ **File transfer system** for sending attachments  
✅ **Clustered RabbitMQ deployment** on AWS EC2 for **high availability**  
✅ **Network Load Balancer (NLB)** to distribute requests across nodes  
✅ **RabbitMQ Web Management API** integration for **system monitoring and analytics**  

---

## Architecture Overview  

### **RabbitMQ & Pub/Sub Model**  
This system is based on the **publish/subscribe (pub/sub) architecture**, which allows multiple users (**subscribers**) to receive messages published to an **Exchange**. Instead of direct communication between sender and receiver, RabbitMQ **routes messages through an Exchange**, which then delivers them to one or multiple Queues based on predefined rules (**bindings**).  

- **Exchanges**: Used to route messages to the appropriate queues.  
- **Queues**: Hold messages until they are consumed.  
- **Bindings**: Define the relationship between exchanges and queues.  

### **How It Works**  
1️⃣ **User sends a message** → The message is serialized using Protocol Buffers and published to a RabbitMQ **Exchange**.  
2️⃣ **RabbitMQ routes the message** → Messages are directed to the correct **Queue(s)** based on the exchange type.  
3️⃣ **Consumers (clients) receive messages** → Messages are deserialized and displayed in real time.  
4️⃣ **File sharing support** → Users can send and receive attachments through RabbitMQ.  
5️⃣ **Scalability with RabbitMQ Cluster** → The system runs multiple RabbitMQ instances on AWS **EC2 nodes** for high availability.  
6️⃣ **Load Balancing** → A **Network Load Balancer (NLB)** ensures even distribution of messages across RabbitMQ nodes.  
7️⃣ **Monitoring & Analytics** → System data is extracted using the **RabbitMQ Web Management API**.  

---

## 🛠️ Technologies Used  

- **Java** – Backend development  
- **RabbitMQ** – Message broker for real-time messaging  
- **Protocol Buffers (protobuf)** – Efficient message serialization  
- **AWS EC2 Cluster** – High-availability RabbitMQ deployment  
- **Network Load Balancer (NLB)** – Distributes traffic across RabbitMQ nodes  
- **RabbitMQ Web Management API** – Extracts system metrics and analytics  

---

## 💻 Installation & Setup  

### **1️⃣ Clone the repository**  
```sh
git clone https://github.com/cainacastro/RabbitChat.git
cd RabbitChat
```

### **2️⃣ Compile the project**
```sh
mvn compile assembly:single
```

### **3️⃣ Run the application**
```sh
java -jar target/ChatRabbitMQ-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 📂 Project Steps & Usage

### For detailed configuration and usage instructions, please refer to the "etapas" folder in the repository.




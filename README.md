# NerdearLa-Falabella
## Workshop Continuous Aggregation

##Previus steps:

###Enviroment Configuration:


```bash
gcloud auth list
gcloud config list project
gcloud config set project myProject
```
###Generate Infrastructure:

```bash
gcloud pubsub topics create $NombreTopico1

gcloud pubsub topics create $NombreTopico2

gcloud pubsub subscriptions create --topic $NombreTopico1 $Subscription1 

gcloud pubsub subscriptions create --topic $NombreTopico2 $Subscription2

gsutil mb gs://BUCKET_NAME
export BUCKET_NAME=<your-unique-name>


```

### How To use
```bash

mvn compile exec:java -Dexec.mainClass=cl.falabella.fund.App -Dexec.cleanupDaemonThreads=false -Dexec.args=" \
--inputSubscription=projects/PROJECT/subscriptions/SUBSCRIPTION \
--project=PROJECT \
--stagingLocation=gs://STG-BUCKET/ \
--tempLocation=gs://TMP-BUCKET/ \
--runner=DataflowRunner \
--gcsFilePath=gs://ORIGIN-PATH \
--outputTopic=projects/PROJECT/topics/TOPIC \
--dataFlowName=nerdear-la-falabella \
--workerMachineType=n1-standard-2"


```


```bash
gcloud pubsub subscriptions pull mySubscription --auto-ack --limit=3
```
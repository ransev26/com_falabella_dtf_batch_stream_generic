# NerdearLa-Falabella

## Workshop Continuous Aggregation

###What is Conituous Aggregation?
Continuous aggregation are summaries of data for a period of time. Some examples of aggregates are the average temperature per day, the maximum CPU utilization per 5 minutes, and the number of visitors on a website per day.
[Description link](https://docs.timescale.com/timescaledb/latest/getting-started/create-cagg/)

###**Hands on focus**
workshop oriented to know the advantages of using continuous aggregation in our data enrichment processes.

###*what is not this workshop?*
is not a workshop for understand machine learning.

##Previus steps:

###Enviroment Configuration:


```bash
gcloud auth list
gcloud config list project
gcloud config set project myProject
```
###Generate Infrastructure:

```bash
export PROJECT = "NOMBRE_DEL_PROYECTO_GCP"
export BUCKET_NAME = "NOMBRE_DEL_BUCKET_A_CREAR_UNICO"
export TOPIC1 = "NOMBRE_DEL_TOPICO_1_A_CREAR"
export TOPIC2 = "NOMBRE_DEL_TOPICO_2_A_CREAR"
export SUBS1 = "NOMBRE_DE_LA_SUBSCRIPTION_1_A_CREAR"
export SUBS2 = "NOMBRE_DE_LA_SUBSCRIPTION_2_A_CREAR"

gsutil mb gs://$BUCKET_NAME

gcloud pubsub topics create $TOPIC1

gcloud pubsub topics create $TOPIC2

gcloud pubsub subscriptions create --topic $TOPIC1 $SUBS1 

gcloud pubsub subscriptions create --topic $NombreTopico2 $SUBS2
```

### How To use
```bash

mvn compile exec:java -Dexec.mainClass=cl.falabella.fund.App -Dexec.cleanupDaemonThreads=false -Dexec.args=" \
--inputSubscription=projects/$PROJECT/subscriptions/$SUBS1 \
--project=$PROJECT \
--stagingLocation=gs://$BUCKET_NAME/stg \
--tempLocation=gs://$BUCKET_NAME/tmp \
--runner=DataflowRunner \
--gcsFilePath=gs://$BUCKET_NAME \
--outputTopic=projects/$PROJECT/topics/$TOPIC2 \
--dataFlowName=nerdear-la-falabella \
--workerMachineType=n1-standard-2"


```


```bash
gcloud pubsub subscriptions pull $SUBS2 --auto-ack --limit=3
```
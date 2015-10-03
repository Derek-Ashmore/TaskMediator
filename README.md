# TaskMediator
TaskMediator facilitates providing partial functionality in the event of an outage by handling exceptions and providing a default response.  The objective is to make it easy for developers to provide 'partial' functionality should a resource not be available.  Often, providing partial functionality leaves a better impression on users than a complete outage.

That said, developers need that outage to be logged and alerts generated so that full functionality can be restored.  TaskMediator seeks to do just that.

## System Requirements
* Java JDK 6.0 or above (it was compiled under JDK 7 using 1.6 as the target source).
* Apache Commons Lang version 3.0 or above

## Installation Instructions  
TaskMediator is easy to install whether you use maven or not.

### Maven Users  
Maven users can find dependency information [here](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22TaskMediator%22). [forthcoming]

### Non-Maven Users  
Include the following jars in your class path:  
* Download the CircuitBreaker jar from [Github](https://github.com/Derek-Ashmore/TaskMediator/releases) and put it in your class path.  
* Insure Apache Commons Lang version 3.0 or above is in your class path.  

## Usage Instructions  
To use TaskMediator, follow these instructions:  
* Create a "callable" that performs an action using an external resource
* Create a default response that will be returned should your Callable error out.
* Use TaskMediator to execute your Callable and return a response.

By default, TaskMediator will log any exceptions in your callable at the ERROR level via Slf4J.  You can provide a TaskErrorHandler with custom reactions to exceptions if needed.

## Usage examples:

### A most basic example with the Slf4j exception logging default
```  
TaskMediator<String> taskMediator = new TaskMediator<String>(DEFAULT_RESPONSE);  
MyCallable callable = new MyCallable();  

String callResult = taskMediator.invoke(callable);
```  

### Example with custom error handler
```  
TaskMediator<String> taskMediator = new TaskMediator<String>(DEFAULT_RESPONSE, 
	new MyTaskErrorHandler());  
MyCallable callable = new MyCallable();  

String callResult = taskMediator.invoke(callable);
```  

 

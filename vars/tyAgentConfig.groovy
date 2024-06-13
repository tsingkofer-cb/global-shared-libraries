#!/usr/bin/env groovy
import com.ty.jenkins.library.yaml.AgentYaml
def call(Map config = [:]){

  def ret = [:]
 
  String defaultLabel = "build-config-${UUID.randomUUID().toString()}"
  String label = config.get('label', defaultLabel)
  String defaultContainer = 'jnlp'
  def templatePaths = []
  def templates = ['base'] + config.get('templates', [])

  templates = templates.unique()
  String template
  templates.each { name ->
    template = libraryResource 'podTemplates/' + name + '.yaml'
    templatePaths.add(template)
  }

  ret['label'] = label
  ret['defaultContainer'] = defaultContainer
  ret['yaml'] = agentYAML(templatePaths)

  return ret
}

def agentYAML(def templatePaths){
  def agentYaml = new AgentYaml()
  return agentYaml.merge(templatePaths)
}

package com.gsgenetics.graph.utility

import com.gsgenetics.genie.locus.Allele
import com.gsgenetics.graph.structure.AlleleNode
import com.gsgenetics.graph.structure.GeneNode
import com.gsgenetics.graph.structure.Labels
import com.gsgenetics.graph.structure.LocusNode
import com.gsgenetics.graph.structure.RelTypes
import com.gsgenetics.graph.structure.Variant
import net.sf.picard.reference.IndexedFastaSequenceFile
import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Path
import org.neo4j.graphdb.ResourceIterable
import org.neo4j.graphdb.Transaction
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.graphdb.traversal.Evaluators
import org.neo4j.graphdb.traversal.TraversalDescription
import org.neo4j.graphdb.traversal.Traverser
import org.neo4j.kernel.Traversal
import org.neo4j.tooling.GlobalGraphOperations

/**
 * Created with IntelliJ IDEA.
 * User: afrieden
 * Date: 7/11/13
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
class GraphHelpers {
    /**
     * Uses a gene name to see if gene node exists in server
     * @param geneName
     * @return
     */
    public static Boolean doesGeneExist(String geneName, GraphDatabaseService graphDb)
    {
        ResourceIterable<org.neo4j.graphdb.Node> geneNodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(Labels.Gene);
        Boolean found = false;
        for(org.neo4j.graphdb.Node geneInstance: geneNodes)
        {
            if(geneInstance.hasProperty(GeneNode.Properties.Name.toString()))
            {

                if(geneInstance.getProperty(GeneNode.Properties.Name.toString()) == geneName)
                {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Gets Graph Database
     * @return
     */
    public static GraphDatabaseService connectToGraph()
    {
        String DB_PATH = GraphHelpers.getDBPath();
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        return graphDb;
    }
    

 

    /**
     * Traverser for getting the parent(incoming relationship) of a node
     * @param reference
     * @param myRelType
     * @return
     */
    public static Traverser getParent(final org.neo4j.graphdb.Node reference, RelTypes myRelType)
    {
        TraversalDescription td = Traversal.description()
                .breadthFirst()
                .relationships( myRelType, Direction.INCOMING )
                .evaluator( Evaluators.toDepth(1) )
                .evaluator( Evaluators.excludeStartPosition());
        return td.traverse( reference );
    }

    /**
     * Traverser for getting the child(outgoing relationship) of a node
     * @param reference
     * @param myRelType
     * @param depth
     * @return
     */
    public static Traverser getChild(final org.neo4j.graphdb.Node reference, RelTypes myRelType, Integer depth)
    {
        TraversalDescription td = Traversal.description()
                .breadthFirst()
                .relationships( myRelType, Direction.OUTGOING )
                .evaluator( Evaluators.toDepth(depth) )
                .evaluator( Evaluators.excludeStartPosition());
        return td.traverse( reference );
    }
    /**
     * Traverser for getting the child(outgoing relationship) of a node
     * @param reference
     * @param myRelType
     * @return
     */
    public static Traverser getChild(final org.neo4j.graphdb.Node reference, RelTypes myRelType)
    {
        TraversalDescription td = Traversal.description()
                .breadthFirst()
                .relationships( myRelType, Direction.OUTGOING )
                .evaluator( Evaluators.excludeStartPosition());
        return td.traverse( reference );
    }
    /**
     * Gets String Path for Graph.DB Directory for Neo4j
     * @return
     */
    public static String getDBPath()
    {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("/change/me/now/prodConfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        final String DB_PATH = prop.getProperty("DB_PATH");
        return DB_PATH;
    }



}

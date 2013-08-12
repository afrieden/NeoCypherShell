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
//                {
                    //does exist
                    found = true;
//                }
            }
        }
        return found;
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
    public static Integer MakeVariant(String CdnaName, String VariantType, String ProteinName, String InheritanceType, String SeqDerivedEvidence, String CurationStatus, String UseAlias, String GsgDiscovered,
                                   String GTAssayed, String NgsAssayed, String ManualReview, String LabDirectorApproved, String PublishNow, String Deleted, String QC_Complete, String geneName,
                                   GraphDatabaseService graphDb, Allele allele) throws Exception {
//        GraphDatabaseService graphDb = connectToGraph();
        Transaction tx = graphDb.beginTx();
        org.neo4j.graphdb.Node geneNode = GraphHelpers.getGeneNode(geneName, graphDb);
        org.neo4j.graphdb.Node variantNode
        try
        {
            variantNode = graphDb.createNode(Labels.Variant);
            //do null checks for each of these
            if (CdnaName != null)
            {
                variantNode.setProperty(Variant.Properties.CdnaName.toString(),CdnaName);
            }
            if (VariantType != null)
            {
                variantNode.setProperty(Variant.Properties.VariantType.toString(),VariantType);
            }
            if(ProteinName != null)
            {
                variantNode.setProperty(Variant.Properties.ProteinName.toString(),ProteinName);
            }
            if(InheritanceType != null)
            {
                variantNode.setProperty(Variant.Properties.InheritanceType.toString(),InheritanceType);
            }
            if (SeqDerivedEvidence != null)
            {
                variantNode.setProperty(Variant.Properties.SeqDerivedEvidence.toString(),SeqDerivedEvidence);
            }
            if (CurationStatus != null)
            {
                variantNode.setProperty(Variant.Properties.CurationStatus.toString(),CurationStatus);
            }
            if(UseAlias != null)
            {
                variantNode.setProperty(Variant.Properties.UseAlias.toString(),UseAlias);
            }
            if (GsgDiscovered != null)
            {
                variantNode.setProperty(Variant.Properties.GsgDiscovered.toString(),GsgDiscovered);
            }
            if (GTAssayed != null)
            {
                variantNode.setProperty(Variant.Properties.GTAssayed.toString(),GTAssayed);
            }
            if (NgsAssayed != null)
            {
                variantNode.setProperty(Variant.Properties.NgsAssayed.toString(),NgsAssayed);
            }
            if (ManualReview != null)
            {
                variantNode.setProperty(Variant.Properties.ManualReview.toString(),ManualReview);
            }
            if (LabDirectorApproved != null)
            {
                variantNode.setProperty(Variant.Properties.LabDirectorApproved.toString(),LabDirectorApproved)
            }
            if (PublishNow != null)
            {
                variantNode.setProperty(Variant.Properties.PublishNow.toString(),PublishNow)
            }
            if (Deleted != null)
            {
                variantNode.setProperty(Variant.Properties.Deleted.toString(),Deleted)
            }
            if (QC_Complete != null)
            {
                variantNode.setProperty(Variant.Properties.QC_Complete.toString(),QC_Complete);
            }

            geneNode.createRelationshipTo(variantNode,RelTypes.IN_GENE);
            //now connect the node
            ResourceIterable<org.neo4j.graphdb.Node> MyAlleleNodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(Labels.Allele);

            for(org.neo4j.graphdb.Node alleleNode: MyAlleleNodes)
            {
                if (alleleNode.getProperty(AlleleNode.Properties.Lesion.toString()) == allele.lesion.name())
                {
                    if (alleleNode.getProperty(AlleleNode.Properties.Pattern.toString()) == allele.pattern)
                    {
                        org.neo4j.graphdb.Node myLocus = GraphHelpers.getChild(alleleNode,RelTypes.CONNECTED, 1).iterator().next().endNode()
                        if (myLocus.hasLabel(Labels.Locus))
                        {
                            //check on coordinates from locus
                            if (myLocus.hasProperty(LocusNode.Contig))
                            {
                                if (myLocus.getProperty(LocusNode.Contig) == allele.contig)
                                {
                                    if (myLocus.getProperty(LocusNode.Lower) == allele.lower)
                                    {
                                        if (myLocus.getProperty(LocusNode.Upper) == allele.upper)
                                        {
                                            alleleNode.createRelationshipTo(variantNode,RelTypes.CONTAINS)
                                        }
                                    }
                                }
                            }

                        }

                    }
                }
            }
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        Integer variantID = variantNode.getId()
        return variantID

    }

    public static org.neo4j.graphdb.Node getGeneNode(String gene, GraphDatabaseService graphDb) throws Exception {
        ResourceIterable<org.neo4j.graphdb.Node> geneNodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(Labels.Gene);
        for(org.neo4j.graphdb.Node geneInstance: geneNodes)
        {
            if(geneInstance.hasProperty(GeneNode.Properties.Name.toString()))
            {
                if (geneInstance.getProperty(GeneNode.Properties.Name.toString()) == gene)
                {
                    return geneInstance;
                }
            }
        }

        throw new Exception("couldn't find gene");
    }
    /**
     * Create Gene Node
     * @param geneName
     */
    public static void MakeGene(String geneName, GraphDatabaseService graphDb)
    {
//        GraphDatabaseService graphDb = GraphHelpers.connectToGraph()
        Transaction tx = graphDb.beginTx();
        try
        {
            org.neo4j.graphdb.Node geneNode = graphDb.createNode(Labels.Gene);
            geneNode.setProperty(GeneNode.Properties.Name.toString(),geneName);
            tx.success();
        }
        finally
        {
            tx.finish();
        }
//        graphDb.shutdown()
    }
    public static void MakeGene(String geneName,String ccds,GraphDatabaseService graphDb)
    {
//        GraphDatabaseService graphDb = GraphHelpers.connectToGraph()
        Transaction tx = graphDb.beginTx();
        try
        {
            org.neo4j.graphdb.Node geneNode = graphDb.createNode(Labels.Gene);
            geneNode.setProperty(GeneNode.Properties.Name.toString(),geneName);
            geneNode.setProperty(GeneNode.Properties.Ccds.toString(),ccds);
            tx.success();
        }
        finally
        {
            tx.finish();
        }
//        graphDb.shutdown()
    }
    public static Boolean doesVariantExist(String variantName, String geneName, GraphDatabaseService graphDb) throws Exception {
        Boolean variantFound = false;
        org.neo4j.graphdb.Node geneNode = getGeneNode(geneName, graphDb);
        Traverser variantTraverser = DataBaseUtility.getChild(geneNode,RelTypes.IN_GENE,1);
        for(Path variantPath: variantTraverser)
        {
            org.neo4j.graphdb.Node endNode = variantPath.endNode();
            if(endNode.hasLabel(Labels.Variant))
            {
                if (endNode.hasProperty(Variant.Properties.CdnaName.toString()))
                {
                    if(endNode.getProperty(Variant.Properties.CdnaName.toString()) == variantFound)
                    {
                        //Variant Does Exist
                        return true;

                    }
                }
            }
        }
        return false;
    }
    /**
     * Builds the Assembly Object from Server
     * @return
     */
    public static IndexedFastaSequenceFile getAssembly()
    {
        Properties prop = new Properties();
        IndexedFastaSequenceFile hg18 = null;
        try {
            prop.load(new FileInputStream("/sandbox/afrieden/MyTools/EmbeddedGraphPlatform/prodConfig.properties"));
            hg18 = new IndexedFastaSequenceFile(new File(prop.getProperty("assembly")));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return hg18;
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
            prop.load(new FileInputStream("/sandbox/afrieden/MyTools/EmbeddedGraphPlatform/prodConfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        final String DB_PATH = prop.getProperty("DB_PATH");
        return DB_PATH;
    }



}

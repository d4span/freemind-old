package de.xeinfach.kafenio.util;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Description: TreeSpider Utility class for traversing, scanning & describing directory/file structures
 *
 * @author Howard Kistler
 */

public class TreeSpider {

	private static LeanLogger log = new LeanLogger("TreeSpider.class");

	private File rootFile;
	private Vector vcFiles;
	private Vector vcExtensions;
	private int maxDepth;
	private boolean bScreenExtensions;
	private boolean bFileNamesOnly;
	private boolean bDisplayMode;
	private FileTreeComparator localComparison;

	/**
	 * constructs a new TreeSpider Object using the given values.
	 * @param origin path origin
	 * @param exts allowed file extensions
	 * @param caseSensitive case sensitive on/off
	 * @param mDepth maximum path depth allowed
	 */
	public TreeSpider(String origin, Vector exts, boolean caseSensitive, int mDepth) {
		rootFile = new File(origin);
		vcFiles = new Vector();
		vcExtensions = exts;
		maxDepth = mDepth;
		bScreenExtensions = false;
		bFileNamesOnly = false;
		bDisplayMode = false;
		if(vcExtensions.size() > 0) {
			bScreenExtensions = true;
		}
		localComparison = new FileTreeComparator(caseSensitive);
	}

	/**
	 * constructs a new TreeSpider Object using the given values.
	 * @param origin path origin
	 * @param exts allowed file extensions
	 * @param caseSensitive case sensitive on/off
	 */
	public TreeSpider(String origin, Vector exts, boolean caseSensitive) {
		this(origin, new Vector(), caseSensitive, 0);
	}

	/**
	 * constructs a new TreeSpider Object using the given values.
	 * @param origin path origin
	 * @param caseSensitive case sensitive on/off
	 * @param mDepth maximum path depth allowed
	 */
	public TreeSpider(String origin, boolean caseSensitive, int mDepth) {
		this(origin, new Vector(), caseSensitive, mDepth);
	}

	/**
	 * constructs a new TreeSpider Object using the given values.
	 * @param origin path origin
	 * @param caseSensitive case sensitive on/off
	 */
	public TreeSpider(String origin, boolean caseSensitive) {
		this(origin, new Vector(), caseSensitive, 0);
	}

	/**
	 * constructs a new TreeSpider Object using the given values.
	 * @param origin path origin
	 * @param mDepth maximum path depth allowed
	 */
	public TreeSpider(String origin, int mDepth) {
		this(origin, new Vector(), false, mDepth);
	}

	/**
	 * constructs a new TreeSpider Object using the given values.
	 * @param origin path origin
	 */
	public TreeSpider(String origin) {
		this(origin, new Vector(), false, 0);
	}

	/**
	 * constructs a new TreeSpider Object using the given values.
	 */
	public TreeSpider() {
		this(".", new Vector(), false, 0);
	}


	/**
	 * @return returns the root file
	 */
	public File getRootFile() {
		return rootFile;
	}

	/**
	 * sets the root file.
	 * @param f a file
	 */
	public void setRootFile(File f) {
		rootFile = f;
	}

	/**
	 * @return returns the list of files
	 */
	public Vector getFiles() {
		return vcFiles;
	}

	/**
	 * sets the list of files
	 * @param vc list of files as vector.
	 */
	public void setFiles(Vector vc) {
		vcFiles = vc;
	}

	/**
	 * @return returns currently set maximum folder depth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * sets the max allowed folder depth
	 * @param i maximum depth
	 */
	public void setMaxDepth(int i) {
		maxDepth = i;
	}

	/**
	 * @return returns true if file extensions are screened, false otherwise.
	 */
	public boolean doScreenExtensions() {
		return bScreenExtensions;
	}

	/**
	 * set to true if file extensions should be screened, false otherwise.
	 * @param b boolean value that defines wether to screen file extensions or not.
	 */
	public void setScreenExtensions(boolean b) {
		bScreenExtensions = b;
	}

	/**
	 * @return returns true if filenames only should be displayed, false otherwise.
	 */
	public boolean showFileNamesOnly() {
		return bFileNamesOnly;
	}

	/**
	 * @param b sets if only filenames should be shown.
	 */
	public void setFileNamesOnly(boolean b) {
		bFileNamesOnly = b;
	}

	/**
	 * @return returns the displaying mode of the files
	 */
	public boolean getDisplayMode() {
		return bDisplayMode;
	}

	/**
	 * sets the display mode.
	 * @param mode can be true or false.
	 */
	public void setDisplayMode(boolean mode) {
		bDisplayMode = mode;
	}

	/**
	 * @return returns valid file extensions as vector.
	 */
	public Vector getExtensions() {
		return vcExtensions;
	}

	/**
	 * set file extensions
	 * @param vc list of file extensions
	 */
	public void setExtensions(Vector vc) {
		vcExtensions = vc;
		if(vcExtensions.size() > 0) {
			setScreenExtensions(true);
		}
		else {
			setScreenExtensions(false);
		}
	}

	/**
	 * Returns a DefaultTreeModel built from the specified root file
	 * @param newRootFile new root file
	 * @return Returns a DefaultTreeModel built from the specified root file
	 */
	public DefaultTreeModel fetchTree(File newRootFile) {
		return describeTree(newRootFile);
	}

	/**
	 * Returns a DefaultTreeModel built from the preset root file
	 * @return Returns a DefaultTreeModel built from the preset root file
	 */
	public DefaultTreeModel fetchTree() {
		return describeTree(rootFile);
	}

	/**
	 * Returns an unsorted listing of the file tree
	 * @return Returns an unsorted listing of the file tree
	 */
	public Vector fetchMassListing() {
		return fetchListing();
	}

	/**
	 * Returns an index-style listing of the file tree of complete file names and paths
	 * @return Returns an index-style listing of the file tree of complete file names and paths
	 */
	public Vector sortMassListing() {
		Vector vcListing = fetchListing();
		Collections.sort(vcListing, localComparison);
		return vcListing;
	}

	/**
	 * Returns an index-style listing of the file tree of file names only
	 * @return Returns an index-style listing of the file tree of file names only
	 */
	public Vector sortFileListing() {
		Vector vcListing = trimToName(fetchListing());
		Collections.sort(vcListing, localComparison);
		return vcListing;
	}

	/**
	 * Returns a directory-style listing of the file tree
	 * @return Returns a directory-style listing of the file tree
	 */
	public Hashtable collectListing() {
		Vector vcListing = fetchListing();
		Hashtable htCollected = new Hashtable();
		for(int i = 0; i < vcListing.size(); i++) {
			String fullname = (String)(vcListing.elementAt(i));
			String filepath = fullname.substring(0, fullname.lastIndexOf(File.separator));
			String filename = fullname.substring(fullname.lastIndexOf(File.separator) + 1, fullname.length());
			if(htCollected.containsKey(filepath)) {
				((Vector)(htCollected.get(filepath))).add(filename);
			}
			else {
				htCollected.put(filepath, new Vector());
				((Vector)(htCollected.get(filepath))).add(filename);
			}
		}
		return htCollected;
	}


/* private methods starting here. */
	private DefaultTreeModel describeTree(File newRootFile) {
		DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(newRootFile.getName());
		DefaultTreeModel treeModel = new DefaultTreeModel(treeRoot);
		if(!(newRootFile.isDirectory())) {
			return treeModel;
		}
		String rootStr = newRootFile.getAbsolutePath().trim() + File.separator;
		Vector vcContents = fetchListing();
		while(vcContents.size() > 0) {
			String nodeName = (String)(vcContents.elementAt(0));
			if(nodeName.startsWith(rootStr)) {
				nodeName = nodeName.substring(rootStr.length());
				if(nodeName.indexOf(File.separator) > -1) {
					String baseNode = nodeName.substring(0, nodeName.indexOf(File.separator));
					describeTreeSubNodes(vcContents, treeModel, treeRoot, rootStr, baseNode);
				}
				else {
					treeModel.insertNodeInto(	new DefaultMutableTreeNode(nodeName),
												treeRoot,
												treeModel.getChildCount(treeRoot));

					vcContents.removeElementAt(0);
				}
			}
			else {
				vcContents.removeElementAt(0);
			}
		}
		return treeModel;
	}

	private void describeTreeSubNodes(	Vector vcNodes,
										DefaultTreeModel treeModel,
										DefaultMutableTreeNode nodeParent,
										String basePath,
										String baseNodeName)
	{
		DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode(baseNodeName);
		treeModel.insertNodeInto(thisNode, nodeParent, treeModel.getChildCount(nodeParent));
		boolean inSubNode = true;
		String fullNodePath = basePath + baseNodeName + File.separator;
		while(inSubNode && vcNodes.size() > 0) {
			String nodeName = (String)(vcNodes.elementAt(0));
			if(nodeName.startsWith(fullNodePath)) {
				nodeName = nodeName.substring(fullNodePath.length());
				if(nodeName.indexOf(File.separator) > -1) {
					String baseNode = nodeName.substring(0, nodeName.indexOf(File.separator));
					describeTreeSubNodes(vcNodes, treeModel, thisNode, fullNodePath, baseNode);
				}
				else {
					treeModel.insertNodeInto(	new DefaultMutableTreeNode(nodeName),
												thisNode,
												treeModel.getChildCount(thisNode));

					vcNodes.removeElementAt(0);
				}
			}
			else {
				inSubNode = false;
			}
		}
	}

	private Vector fetchListing() {
		Vector vcFetch = new Vector();
		if(rootFile.isDirectory()) {
			vcFetch.add(exploreFolder("", rootFile, new Vector(), 1));
		}
		else {
			String fullFile = rootFile.getAbsolutePath().trim();
			if(!bScreenExtensions || (bScreenExtensions && screensOkay(fullFile))) {
				vcFetch.add(fullFile);
			}
		}
		return describeListing(vcFetch);
	}

	private Vector trimToName(Vector fullFiles) {
		Vector vcReturn = new Vector(fullFiles.size());
		for(int i = 0; i < fullFiles.size(); i++) {
			String trimName = (String)(fullFiles.elementAt(i));
			if(trimName.indexOf(File.separator) > -1) {
				vcReturn.add(trimName.substring(trimName.lastIndexOf(File.separator) + 1, trimName.length()));
			}
			else {
				vcReturn.add(trimName);
			}
		}
		return vcReturn;
	}

	/**
	 * Recursively explores file folders and subfolders, adding all files which match the filtered type
	 * to the returning Vector.
	 */
	private Vector exploreFolder(String currentLocal, File folder, Vector vcWorking, int currDepth) {
		if((maxDepth != 0) && currDepth > maxDepth) {
			return vcWorking;
		}
		String fullDescriptor = currentLocal + File.separator + folder.getName();
		File[] contents = folder.listFiles();
		for(int i = 0; i < contents.length; i++) {
			if(contents[i].isDirectory()) {
				vcWorking.add(exploreFolder(fullDescriptor, contents[i], new Vector(), currDepth + 1));
			}
			else {
				String completeName = contents[i].getAbsolutePath();
				if(bFileNamesOnly) { completeName = contents[i].getName(); }
				if(!bScreenExtensions || (bScreenExtensions && screensOkay(completeName))) {
					vcWorking.add(completeName.trim());
				}
			}
		}
		Collections.sort(vcWorking, localComparison);
		return vcWorking;
	}

	/**
	 * Explores only current file folder, adding all files which match the filtered type
	 * to the returning Vector.
	 */
	private Vector exploreFolderOnly(String currentLocal, File folder, Vector vcWorking) {
		String fullDescriptor = currentLocal + File.separator + folder.getName();
		File[] contents = folder.listFiles();
		for(int i = 0; i < contents.length; i++) {
			if(!(contents[i].isDirectory())) {
				String completeName = contents[i].getAbsolutePath();
				if(bFileNamesOnly) { completeName = contents[i].getName(); }
				if(!bScreenExtensions || (bScreenExtensions && screensOkay(completeName))) {
					vcWorking.add(completeName.trim());
				}
			}
		}
		Collections.sort(vcWorking, localComparison);
		return vcWorking;
	}

	private Vector describeListing(Vector vcTree) {
		Collections.sort(vcTree, localComparison);
		Vector vcDescription = new Vector();
		for(int i = 0; i < vcTree.size(); i++) {
			if(vcTree.elementAt(i) instanceof Vector) {
				describeNode((Vector)(vcTree.elementAt(i)), vcDescription);
			}
			else {
				vcDescription.add(vcTree.elementAt(i));
			}
		}
		return vcDescription;
	}

	private void describeNode(Vector vcNode, Vector vcStore) {
		for(int i = 0; i < vcNode.size(); i++) {
			if(vcNode.elementAt(i) instanceof Vector) {
				describeNode((Vector)(vcNode.elementAt(i)), vcStore);
			}
			else {
				vcStore.add(vcNode.elementAt(i));
			}
		}
	}

	private boolean screensOkay(String filename) {
		for(int i = 0; i < vcExtensions.size(); i++) {
			String extension = ((String)(vcExtensions.elementAt(i))).toLowerCase();
			if(filename.toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Unit test method for testing the code with example settings
	 * @param dir directory for unit testing.
	 */
	public static void unitTest(String dir) {
		Vector vcCommonExts = new Vector();
		vcCommonExts.add(".java");
		vcCommonExts.add(".class");
		vcCommonExts.add(".txt");
		vcCommonExts.add(".html");
		vcCommonExts.add(".htm");
		vcCommonExts.add(".gif");
		vcCommonExts.add(".jpeg");
		vcCommonExts.add(".jpg");
		TreeSpider testSpider = new TreeSpider(dir, vcCommonExts, false, 0);
		log.debug("UNIT TEST SCAN");
		Hashtable htColl = testSpider.collectListing();
		Enumeration enumKeys = htColl.keys();
		while(enumKeys.hasMoreElements()) {
			String key = (String)(enumKeys.nextElement());
			log.debug(key);
			Vector vcValues = (Vector)(htColl.get(key));
			for(int i = 0; i < vcValues.size(); i++) {
				log.debug("  " + vcValues.elementAt(i));
			}
		}
	}

	/**
	 * Class'es main() method.
	 * @param args commandline arguments.
	 */
	public static void main(String[] args) {
		if(args.length < 1) {
			try {
				TreeSpider.unitTest((new File(".")).getCanonicalPath());
			}
			catch(java.io.IOException ioe) {
				log.error("Unable to resolve current directory. Please specify a directory explicitly.");
				System.exit(1);
			}
		} else {
			TreeSpider.unitTest(args[0]);
		}
	}

	/**
	 *  Class for sorting file listings by directory then name
	 */
	class FileTreeComparator implements Comparator
	{
		private boolean isCaseSensitive;

		public FileTreeComparator(boolean caseCheck) {
			isCaseSensitive = caseCheck;
		}

		public FileTreeComparator() {
			isCaseSensitive = true;
		}

		public int compare(Object objA, Object objB) {
			if(objA instanceof Vector) {
				if(objB instanceof Vector) {
					if(((Vector)(objA)).size() < 1)
					{
						return -1;
					}
					else if(((Vector)(objB)).size() < 1)
					{
						return 1;
					}
					else
					{
						return compare(((Vector)(objA)).elementAt(0), ((Vector)(objB)).elementAt(0));
					}
				}
				else {
					return 1;
				}
			}
			else if(objB instanceof Vector) {
				return -1;
			}
			else {
				String strA = (String)objA;
				String strB = (String)objB;
				return (isCaseSensitive ? strA.compareTo(strB) : strA.compareToIgnoreCase(strB));
			}
		}
	}

}
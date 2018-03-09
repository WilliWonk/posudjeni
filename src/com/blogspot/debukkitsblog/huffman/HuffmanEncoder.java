package com.blogspot.debukkitsblog.huffman;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Huffman encodes characters based on their frequency of occurance
 * @author DeBukkIt
 */
public class HuffmanEncoder {

	/**
	 * Main method for test purposes
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		// test input
		HashMap<Character, String> result = generateHofmannEncoding(
				CharacterCounter.countCharacters("Guten Morgen, liebe Sorgen, seid ihr auch schon wieder da? Hast du auch so gut geschlafen? Na, dann ist ja alles klar.")
		);

		// test output
		for (Character key : result.keySet()) {
			System.out.println(key + ": " + result.get(key));
		}

	}

	/**
	 * Generates a HashMap containing characters and their binary Huffman encoding
	 * 
	 * @param tupelList
	 *            A list of <i>HufmannTuple</i>s (characters and their frequency)
	 *            the encoding should be generated with
	 * @return A HashMap containing the characters and their binary Huffman encoding
	 */
	public static HashMap<Character, String> generateHofmannEncoding(HuffmanTuple... tupelList) {

		HashMap<Character, String> result = new HashMap<>();
		List<BinTree> liste = new ArrayList<BinTree>();

		// add all pairs of characters and their relative frequency to the list
		for (HuffmanTuple cTupel : tupelList) {
			liste.add(new BinTree(cTupel));
		}

		// specified how the list should be sorted
		Comparator<BinTree> comp = new Comparator<BinTree>() {
			@Override
			public int compare(BinTree o1, BinTree o2) {
				HuffmanTuple t1 = (HuffmanTuple) o1.getContent();
				HuffmanTuple t2 = (HuffmanTuple) o2.getContent();
				return t1.getFrequency() - t2.getFrequency();
			}
		};

		// build a Hofmann tree
		while (liste.size() > 1) {

			// sort the list
			liste.sort(comp);

			// get the smallest two elements (and remove them from the list)
			BinTree elem1 = liste.remove(0);
			BinTree elem2 = liste.remove(0);

			// sum up their relative frequencies
			HuffmanTuple t1 = (HuffmanTuple) elem1.getContent();
			HuffmanTuple t2 = (HuffmanTuple) elem2.getContent();

			// generate a new node as parent of the two smaller nodes
			BinTree elemNew = new BinTree(new HuffmanTuple(null, t1.getFrequency() + t2.getFrequency()));
			elemNew.setLeftChild(elem1);
			elemNew.setRightChild(elem2);

			// add the new node to the list
			liste.add(elemNew);
		}

		// generate a table of the characters and their encodings
		for (HuffmanTuple cTupel : tupelList) {
			result.put(cTupel.getCharacter(), getEncoding(liste.get(0), cTupel.getCharacter()));
		}

		return result;
	}

	/**
	 * Finds the given character in a <i>BinTree</i> and inspects its path
	 * vertically to the mainRoot remembering the left and right turns and so
	 * generating the character's Huffman encoding
	 * 
	 * @param mainRoot
	 *            The root of the <i>BinTree</i> to be inspected
	 * @param character
	 *            The character
	 * @return The character's Huffman encoding as a String contains binary digits
	 */
	private static String getEncoding(BinTree mainRoot, Character character) {
		String verticalTraversalString = "";

		// find wanted character in tree
		BinTree currentParent = mainRoot.find(new HuffmanTuple(character, -1));
		// walk the path from that node to the tree's main root
		while (currentParent != null && currentParent.getPredecessor() != null) {
			// remember the left-right-turns on the path
			verticalTraversalString = currentParent.getPredecessorPathName().getRepresentingDigit() + verticalTraversalString;
			currentParent = currentParent.getPredecessor();
		}

		return verticalTraversalString;
	}

}
package associate;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class ProperSubsetCombination {
	private static String[] array;
	private static BitSet startBitSet; // ���ؼ�����ʼ״̬
	private static BitSet endBitSet; // ���ؼ�����ֹ״̬����������ѭ��
	private static Set<Set<String>> properSubset; // ���Ӽ�����

	public static Set<Set<String>> getProperSubset(int n, Set<String> itemSet)
	{
		String[] array = new String[itemSet.size()];
		ProperSubsetCombination.array = itemSet.toArray(array);
		properSubset = new HashSet<Set<String>>();
		startBitSet = new BitSet();
		endBitSet = new BitSet();
		// ��ʼ��startBitSet�����ռ��1
		for (int i = 0; i < n; i++)
		{
			startBitSet.set(i, true);
		}
		// ��ʼ��endBit���Ҳ�ռ��1
		for (int i = array.length - 1; i >= array.length - n; i--)
		{
			endBitSet.set(i, true);
		}

		// �����ʼstartBitSet����һ����ϼ��뵽���Ӽ�������
		get(startBitSet);

		while (!startBitSet.equals(endBitSet))
		{
			int zeroCount = 0; // ͳ������10�����0�ĸ���
			int oneCount = 0; // ͳ������10�����1�ĸ���
			int pos = 0; // ��¼��ǰ����10������λ��

			// ����startBitSet��ȷ��10���ֵ�λ��
			for (int i = 0; i < array.length; i++)
			{
				if (!startBitSet.get(i))
				{
					zeroCount++;
				}
				if (startBitSet.get(i) && !startBitSet.get(i + 1))
				{
					pos = i;
					oneCount = i - zeroCount;
					// ��10��Ϊ01
					startBitSet.set(i, false);
					startBitSet.set(i + 1, true);
					break;
				}
			}
			// ������10������1ȫ���ƶ��������
			int counter = Math.min(zeroCount, oneCount);
			int startIndex = 0;
			int endIndex = 0;
			if (pos > 1 && counter > 0)
			{
				pos--;
				endIndex = pos;
				for (int i = 0; i < counter; i++)
				{
					startBitSet.set(startIndex, true);
					startBitSet.set(endIndex, false);
					startIndex = i + 1;
					pos--;
					if (pos > 0)
					{
						endIndex = pos;
					}
				}
			}
			get(startBitSet);
		}
		return properSubset;
	}

	private static void get(BitSet bitSet)
	{
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < array.length; i++)
		{
			if (bitSet.get(i))
			{
				set.add(array[i]);
			}
		}
		properSubset.add(set);
	}
}
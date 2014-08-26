/*
 * Copyright 1997-2014 Optimatika (www.optimatika.se)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.matrix;

import org.ojalgo.TestUtils;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.jama.JamaEigenvalue;
import org.ojalgo.matrix.store.MatrixStore;

/**
 * Gilbert Strang, Linear Algebra and its Applications III, Chapter 5
 * 
 * @author apete
 */
public class SimpleEigenvalueCase extends BasicMatrixTest {

    public static BigMatrix getMatrixD() {
        final BigMatrix tmpMtrx = BigMatrix.FACTORY.rows(new double[][] { { 2.0, 0.0 }, { 0.0, -1.0 } });
        return tmpMtrx.enforce(DEFINITION);
    }

    public static BigMatrix getMatrixV() {
        final BigMatrix tmpMtrx = BigMatrix.FACTORY.rows(new double[][] { { 5.0, 1.0 }, { 2.0, 1.0 } });
        return tmpMtrx.enforce(DEFINITION);
    }

    public static BigMatrix getOriginal() {
        final BigMatrix tmpMtrx = BigMatrix.FACTORY.rows(new double[][] { { 4.0, -5.0 }, { 2.0, -3.0 } });
        return tmpMtrx.enforce(DEFINITION);
    }

    public SimpleEigenvalueCase() {
        super();
    }

    public SimpleEigenvalueCase(final String arg0) {
        super(arg0);
    }

    @Override
    public void testData() {

        myExpMtrx = SimpleEigenvalueCase.getOriginal().multiplyRight(SimpleEigenvalueCase.getMatrixV());

        myActMtrx = SimpleEigenvalueCase.getMatrixV().multiplyRight(SimpleEigenvalueCase.getMatrixD());

        TestUtils.assertEquals(myExpMtrx, myActMtrx, EVALUATION);
    }

    @Override
    public void testProblem() {

        final Eigenvalue<Double> tmpEigen = new JamaEigenvalue.General();
        tmpEigen.compute(SimpleEigenvalueCase.getOriginal().toPrimitiveStore());

        final MatrixStore<Double> tmpV = tmpEigen.getV();
        final MatrixStore<Double> tmpD = tmpEigen.getD();

        myExpMtrx = SimpleEigenvalueCase.getMatrixD();
        myActMtrx = PrimitiveMatrix.FACTORY.copy(tmpD);

        TestUtils.assertEquals(myExpMtrx, myActMtrx, EVALUATION);

        final BasicMatrix tmpMtrx = SimpleEigenvalueCase.getMatrixV().divideElements(PrimitiveMatrix.FACTORY.copy(tmpV));

        double tmpExp;
        double tmpAct;
        final double tmpErr = EVALUATION.error();
        for (int j = 0; j < tmpMtrx.countColumns(); j++) {
            tmpExp = tmpMtrx.doubleValue(0, j);
            for (int i = 0; i < tmpMtrx.countRows(); i++) {
                tmpAct = tmpMtrx.doubleValue(i, j);
                TestUtils.assertEquals(tmpExp, tmpAct, tmpErr);
            }
        }

        TestUtils.assertEquals(myExpMtrx, myActMtrx, EVALUATION);
    }

    @Override
    protected void setUp() throws Exception {

        DEFINITION = TestUtils.EQUALS.newScale(1);
        EVALUATION = TestUtils.EQUALS.newScale(3);

        myBigAA = SimpleEigenvalueCase.getOriginal();
        myBigAX = SimpleEigenvalueCase.getMatrixV();
        myBigAB = SimpleEigenvalueCase.getMatrixV().multiplyRight(SimpleEigenvalueCase.getMatrixD());

        myBigI = BasicMatrixTest.getIdentity(myBigAA.countRows(), myBigAA.countColumns(), DEFINITION);
        myBigSafe = BasicMatrixTest.getSafe(myBigAA.countRows(), myBigAA.countColumns(), DEFINITION);

        super.setUp();
    }

}

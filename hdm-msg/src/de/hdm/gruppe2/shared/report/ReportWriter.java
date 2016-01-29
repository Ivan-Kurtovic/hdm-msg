package de.hdm.gruppe2.shared.report;

/**
 * <p>
 * Diese Klasse wird ben�tigt, um auf dem Client die ihm vom Server zur
 * Verf�gung gestellten <code>Report</code>-Objekte in ein menschenlesbares
 * Format zu �berf�hren.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat �berf�hrten Information wird den Subklassen �berlassen. In
 * dieser Klasse werden die Signaturen der Methoden deklariert, die f�r die
 * Prozessierung der Quellinformation zust�ndig sind.
 * </p>
 * 
 * @author Thies
 * @author Kurtovic
 */
public abstract class ReportWriter {

  /**
   * �bersetzen eines <code>AllMessagesOfUserReport</code> in das
   * Zielformat.
   * 
   * @param r der zu �bersetzende Report
   */
  public abstract void process(AllMessagesOfUserReport r);

  /**
   * �bersetzen eines <code>AllMessagesOfAllUsers</code> in das
   * Zielformat.
   * 
   * @param r der zu �bersetzende Report
   */
  public abstract void process(AllMessagesOfAllUsersReport r);
  
  /**
   * �bersetzen eines <code>AllMessagesOfPeriodReport</code> in das
   * Zielformat.
   * 
   * @param r der zu �bersetzende Report
   */
  public abstract void process(AllMessagesOfPeriodReport r);
  
  /**
   * �bersetzen eines <code>AllFollowersOfUserReport</code> in das
   * Zielformat.
   * 
   * @param r der zu �bersetzende Report
   */
  public abstract void process(AllFollowersOfUserReport r);
  
  /**
   * �bersetzen eines <code>AllFollowersOfHashtagReport</code> in das
   * Zielformat.
   * 
   * @param r der zu �bersetzende Report
   */
  public abstract void process(AllFollowersOfHashtagReport r);

}
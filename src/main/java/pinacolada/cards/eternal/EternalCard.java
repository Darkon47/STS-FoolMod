package pinacolada.cards.eternal;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.PGR;

public class EternalCard extends PCLCard {

    protected EternalCard(PCLCardData cardData) {
        super(cardData);
    }

    protected EternalCard(PCLCardData cardData, int form, int timesUpgraded) {
        super(cardData, form, timesUpgraded);
    }

    protected static PCLCardData Register(Class<? extends PCLCard> type)
    {
        return RegisterCardData(type, PGR.Eternal.CreateID(type.getSimpleName())).SetColor(PGR.Eternal.CardColor);
    }

    public boolean CheckPrimaryCondition(boolean tryUse) {
        return PCLCombatStats.MatchingSystem.ResolveMeter.InUltimateMode();
    }

    public void SetLight() {
        SetAffinity_Light(1);
    }

    public void SetDark() {
        SetAffinity_Dark(1);
    }

    public void SetNeutral() {
        SetAffinity_Silver(1);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (PCLCombatStats.MatchingSystem.ResolveMeter.InUltimateMode()) {
            costForTurn = 0;
            isCostModifiedForTurn = true;
        }
    }

    @Override
    protected Texture GetCardBackground()
    {
        switch (type)
        {
            case ATTACK: return isPopup ? PGR.Eternal.Images.CARD_BACKGROUND_ATTACK_L.Texture() : PGR.Eternal.Images.CARD_BACKGROUND_ATTACK.Texture();
            case POWER: return isPopup ?PGR.Eternal.Images.CARD_BACKGROUND_POWER_L.Texture() : PGR.Eternal.Images.CARD_BACKGROUND_POWER.Texture();
            default: return isPopup ? PGR.Eternal.Images.CARD_BACKGROUND_SKILL_L.Texture() : PGR.Eternal.Images.CARD_BACKGROUND_SKILL.Texture();
        }
    }

    public static boolean IsMismatching(CardUseInfo info) {
        PCLAffinity af = PCLCombatStats.MatchingSystem.GetActiveMeter().GetCurrentAffinity();
        return !info.IsSynergizing && af != PCLAffinity.General && af != PCLAffinity.Silver;
    }
}
